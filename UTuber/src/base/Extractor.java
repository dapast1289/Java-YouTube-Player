package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Extractor {

	final static String fmt = "A\", \"url_encoded_fmt_stream_map\": \"type=vi\"";

	public static void main(String[] args) {
		String[] searched = Utubr.Search("katy perry").get(0);
		System.out.println(searched[1]);
		System.out.println(searched[0]);
		VidPage vp = extract(searched[0], searched[1]);
		System.out.println(vp.getDecodedStream(0));
		
	}
	
	public static String decodeSignature(String sig, String playerURLString) {
		if (sig == null) {
			return null;
		}
		try {
			System.out.println("signature: " + sig);
			URL pURL = new URL(playerURLString);
			URLConnection pUC = pURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(pUC.getInputStream()));
			String playerPage = "";
			String s = "";
			while ((s = in.readLine()) != null) {
				playerPage += s;
			}
			int methodnamepos = playerPage.indexOf("signature=") + 10;
			String sigMethod = playerPage.substring(methodnamepos, methodnamepos + 2);
			System.out.println("Signature method name= " + sigMethod);
			
			int methodpos = playerPage.indexOf("function " + sigMethod);
			String decryptionMethod = playerPage.substring(methodpos);
			decryptionMethod = decryptionMethod.split("}")[0] + "}";
			System.out.println(decryptionMethod);
			
			ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
			Invocable inv = (Invocable) se; 
			se.eval(decryptionMethod);
			String decryptedSig = (String) inv.invokeFunction(sigMethod, sig);
			return decryptedSig;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static VidPage extract(String urlString, String title) {
		if (!validiteURL(urlString)) {
			System.err.println("Invalid url provided: " + urlString);
			return null;
		}
		URL url;
		try {
			url = new URL(urlString);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String parsedSite = urlToString(url);
		String streamMap = getStreamMap(parsedSite);
		String playerURL = getPlayerURL(parsedSite);
		if (!playerURL.contains("https:")) {
			playerURL = "https:" + playerURL;
		}
		System.out.println("playerURL: " + playerURL);

		String tempurl = null;
		String tempquality = null;
		String tempsig = null;
		int tempitag = -1;
		ArrayList<VideoStream> vidArray = new ArrayList<VideoStream>();

		for (String streamMapVidString : streamMap.split(",")) {
			for (String string : streamMapVidString.split("\\\\u0026")) {
				if (string.startsWith("s=")) {
					tempsig = string.split("s=")[1];
				}
				if (string.contains("url=")) {
					try {
						tempurl = URLDecoder.decode(string.split("url=")[1],
								"UTF-8");
						if (!tempurl.contains("&ratebypass=yes")) {
							tempurl += "&ratebypass=yes";
						}
						if (!tempurl.contains("&title=")) {
							tempurl += "&title=" + URLEncoder.encode(title, "UTF-8");
						}
						
//						System.out.println(tempurl);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else if (string.contains("quality=")) {
					tempquality = string.split("quality=")[1];
				} else if (string.matches("itag=[0-9]{2,3}")) {
					tempitag = Integer.parseInt(string.substring(5));
				}
			}
			VideoStream tempvidStream = new VideoStream(tempurl, tempquality, tempitag, tempsig);
			vidArray.add(tempvidStream);
		}
		return new VidPage(vidArray, playerURL);
	}

	public static boolean validiteURL(String url) {
		return url
				.matches("(https?://)?(www\\.)?youtube\\..{2,3}/.*watch\\?.*v=(.*?)$");
	}

	public static String getStreamMap(String youtubePage) {
		try {
			return youtubePage.split("url_encoded_fmt_stream_map\": \"")[1]
					.split("\"")[0];
		} catch (Exception e) {
			System.err.println("No fmt stream map matched ");
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getPlayerURL(String youtubePage) {
		Pattern pattern = Pattern.compile("assets.*?js\": \"(.*?)\"");
		Matcher matcher = pattern.matcher(youtubePage);
		if (matcher.find()) {
			String playerURL = matcher.group(1);
			if (!playerURL.contains("https:")) {
				playerURL = "https:" + playerURL.replace("\\/", "/");
				return playerURL;
			}
		}
		return null;
	}

	public static String urlToString(URL url) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openConnection().getInputStream()));
			String s;
			String parsedString = "";
			while ((s = in.readLine()) != null) {
				parsedString += s;
			}
			return parsedString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
