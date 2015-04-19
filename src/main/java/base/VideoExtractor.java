package base;

import jsinterpreter.JsInterpreter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;

/**
 * Created by Jaap Heijligers on 3/24/15.
 */
public class VideoExtractor {

    final static Pattern FMT_MAP_PATTERN = Pattern.compile("ytplayer\\.config = (\\{.*?\\});");
    final static Pattern HTML5PLAYER_METHOD_NAME_PATTERN = Pattern.compile("sig\\|\\|(..)\\(");
    final static Pattern SECONDARY_METHOD_PATTERN = Pattern.compile(";(\\w{2})\\.\\w{2}\\(");
    final static Pattern FMT_URL_PATTERN = Pattern.compile("url=(.*?)(&|$)");
    final static Pattern FMT_SIG_PATTERN = Pattern.compile("(^|&)s=(.*?)(&|$)");
//    final static ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("javascript");
    private static VideoExtractor extractor;
    private CacheDatabase cacheDatabase;
    private VideoExtractor() {
        cacheDatabase = new CacheDatabase();
    }

    public static void main(String[] args) throws Exception {
//        String url = new VideoExtractor().extractAudio("https://www.youtube.com/watch?v=qQrgto184Tk");
        String url = new VideoExtractor().extractVideo("https://www.youtube.com/watch?v=qQrgto184Tk");
        System.out.println(url);
    }

    public static synchronized VideoExtractor getInstance() {
        if (extractor == null) {
            extractor = new VideoExtractor();
        }
        return extractor;
    }

    public String extractAudio(String youtubeURL) throws Exception {
        System.out.print("Downloading " + youtubeURL + " ...");
        String page = IOUtils.toString(new URL(youtubeURL.replace("http:", "https:")).openStream());
        System.out.println("OK");

        Matcher m = FMT_MAP_PATTERN.matcher(page);
        if (!m.find()) throw new ExtractionError("No FMT map found");

        JSONObject root = new JSONObject(m.group(1));
        String adaptiveFmts = getAdaptiveFmtsAudio(root);
        String streamURL = getStreamUrl(adaptiveFmts) + "&ratebypass=yes";;

        if (streamURL.contains("signature=")) return streamURL;

        String encryptedSignature = getSig(adaptiveFmts);
        String html5PlayerURL = getHtml5PlayerURL(root);
        return prepareStream(root, streamURL, encryptedSignature, html5PlayerURL);
    }

    public String extractVideo(String youtubeURL) throws Exception {
        System.out.print("Downloading " + youtubeURL + " ...");
        String page = IOUtils.toString(new URL(youtubeURL.replace("http:", "https:")).openStream());
        System.out.println("OK");

        Matcher m = FMT_MAP_PATTERN.matcher(page);
        if (!m.find()) throw new ExtractionError("No FMT map found");

        JSONObject root = new JSONObject(m.group(1));
        String fmtStreamMap = getFmtStreamMap(root);
        String streamURL = getStreamUrl(fmtStreamMap);
        if (!streamURL.contains("ratebypass"))
            streamURL = streamURL + "&ratebypass=yes";

        if (streamURL.contains("signature=")) return streamURL;

        String encryptedSignature = getSig(fmtStreamMap);
        String html5PlayerURL = getHtml5PlayerURL(root);
        return prepareStream(root, streamURL, encryptedSignature, html5PlayerURL);
    }

    private String prepareStream(JSONObject root, String streamURL, String encryptedSignature, String html5PlayerURL) throws Exception {
        String playerID = html5PlayerURL.split("/")[5];
        String method;
        if (cacheDatabase.hasMethod(playerID)) {
            System.out.println("Get " + playerID + " from cache");
            method = cacheDatabase.getMethod(playerID);
        } else {
            System.out.print("Downloading " + html5PlayerURL + " ...");
            String html5Player = IOUtils.toString(new URL(html5PlayerURL));
            System.out.println("OK");
            String methodName = getMethodName(html5Player);
            method = getMethod(methodName, html5Player);
            cacheDatabase.addMethod(playerID, method);
        }

        String signature = new JsInterpreter().execute(method, encryptedSignature);
        return streamURL + "&signature=" + signature;
    }

    private String getAdaptiveFmtsFirst(JSONObject root) {
        String encodedFmtStreamMap = root.getJSONObject("args").getString("adaptive_fmts");
        return StringEscapeUtils.unescapeJson(encodedFmtStreamMap.split(",")[0]);
    }

    private String getAdaptiveFmtsAudio(JSONObject root) {
        String encodedFmtStreamMap = root.getJSONObject("args").getString("adaptive_fmts");
        String[] streams = encodedFmtStreamMap.split(",");
        String audioStream = null;
        for (String stream : streams) {
            if (stream.contains("type=audio")) {
                audioStream = stream;
            }
        }
        return StringEscapeUtils.unescapeJson(audioStream);
    }

    private String getFmtStreamMap(JSONObject root) {
        String encodedFmtStreamMap = root.getJSONObject("args").getString("url_encoded_fmt_stream_map");
        return StringEscapeUtils.unescapeJson(encodedFmtStreamMap.split(",")[0]);
    }

    private String getHtml5PlayerURL(JSONObject root) {
        return "https:" + root.getJSONObject("assets").getString("js");
    }

    private String getMethodName(String html5Player) throws ExtractionError {
        Matcher matcher = HTML5PLAYER_METHOD_NAME_PATTERN.matcher(html5Player);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new ExtractionError("Method name not found");
    }

    private String getMethod(String methodName, String html5player) throws ExtractionError {
        Pattern methodPattern = Pattern.compile("function " + methodName + "\\(.\\)\\{(.*?)\\};");
        Matcher methodMatcher = methodPattern.matcher(html5player);
        if (!methodMatcher.find()) throw new ExtractionError("Method not found: " + methodName);

        String method = methodMatcher.group();
        method = method.replace("function " + methodName, "function main");

        Matcher secondaryMethodMatcher = SECONDARY_METHOD_PATTERN.matcher(method);

        if (secondaryMethodMatcher.find()) {
            String secondaryMethodName = secondaryMethodMatcher.group(1);
            if (secondaryMethodName.equals(methodName)) return method;
            return method + getVar(secondaryMethodMatcher.group(1), html5player);
        } else {
            return method;
        }
    }

    private String getVar(String varName, String html5player) throws ExtractionError {
        Pattern varPattern = Pattern.compile("var " + varName + "=\\{(.*?)\\};");
        Matcher varMatcher = varPattern.matcher(html5player);
        if (!varMatcher.find()) throw new ExtractionError("Var not found " + varName);
        return varMatcher.group();
    }

    private String getSig(String fmtStreamMap) throws ExtractionError {
        Matcher m = FMT_SIG_PATTERN.matcher(fmtStreamMap);
        if (!m.find()) throw new ExtractionError("Signature not found: \n" + fmtStreamMap);
        return m.group(2);
    }

    private String getStreamUrl(String fmtStreamMap) throws ExtractionError, UnsupportedEncodingException {
        Matcher m = FMT_URL_PATTERN.matcher(fmtStreamMap);
        if (!m.find()) throw new ExtractionError("Stream URL not found");
        return URLDecoder.decode(m.group(1), "UTF-8");
    }
}

class ExtractionError extends Exception {
    public ExtractionError(String msg) {
        super(msg);
    }
}
