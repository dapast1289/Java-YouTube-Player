package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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
		String[] searched = Utubr.Search("like the way you lie").get(0);
		VidPage vp = extractFmt(searched[0], searched[1]);
		System.out.println(vp.getAudioStream());
		
	}
	
	public static VidPage extractFmt(String urlString, String title) {
		URL url;
		try {
			url = new URL(urlString);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String parsedSite = urlToString(url);
		String fmtString = getFmtList(parsedSite);
		String[] fmtStringList = fmtString.split("(,)");
		
		String playerURL = getPlayerURL(parsedSite);
		
		ArrayList<MediaStream> mediaArray = new ArrayList<MediaStream>();
		
		for (String string : fmtStringList) {
			mediaArray.add(parseFmt(string, title));
		}
		
		VidPage vp = new VidPage(mediaArray, playerURL);
		
		return vp;
	}
	
	public static MediaStream parseFmt(String fmtString, String title) {
		String[] list = fmtString.split("\\\\u0026");
		String size = null;
		int itag = -1;
		String url = null;
		String sig = null;
		String type = null;
		
		for (String string : list) {
			if (string.startsWith("type")) {
				type = string.substring(5).split("\\%3B")[0].replace("%2F", ";");
				System.out.println("type: " + type);
			}
			if (string.startsWith("url=")) {
				try {
					url = URLDecoder.decode(string.substring(4), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (!url.contains("&ratebypass=yes")) {
					url += "&ratebypass=yes";
				}
				if (!url.contains("&title=")) {
					try {
						url += "&title=" + URLEncoder.encode(title, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
			}
			if (string.startsWith("s=")) {
				sig = string.substring(2);
			}
			if (string.startsWith("itag=")) {
				itag = Integer.parseInt(string.substring(5));
			}
			
			if (string.startsWith("size=")) {
				size = string.substring(5);
			}
		}
		MediaStream returnStream = new MediaStream(url, size, itag, sig, type);
		System.out.println("returnStream= " + returnStream.toString());
		return returnStream;
	}
	
	public static String decodeSignature(String sig, String playerURLString) {
		if (sig == null) {
			return null;
		}
		String playerPage = "";
		ScriptEngine se = null;
		Invocable inv = null;
		String decryptionMethod = "";
		String sigMethod = "";
		try {
//			System.out.println("signature: " + sig);
			URL pURL = new URL(playerURLString);
			playerPage = urlToString(pURL);
			int methodnamepos = playerPage.indexOf("signature=") + 10;
			sigMethod = playerPage.substring(methodnamepos, methodnamepos + 2);
//			System.out.println("Signature method name= " + sigMethod);
			
			int methodpos = playerPage.indexOf("function " + sigMethod);
			decryptionMethod = playerPage.substring(methodpos);
			decryptionMethod = decryptionMethod.split("}")[0] + "};";
//			System.out.println(decryptionMethod);
			
			se = new ScriptEngineManager().getEngineByName("javascript");
			inv = (Invocable) se; 
			se.eval(decryptionMethod);
//			System.out.println(sigMethod + " - " + decryptionMethod);
			String decryptedSig = (String) inv.invokeFunction(sigMethod, sig);
			return decryptedSig;
			
		} catch (Exception e) {
			String errormessage = e.getMessage();
			if (errormessage.startsWith("ReferenceError")) {
//				System.out.println("ReferenceError: " + errormessage);
				String newSigMethod = errormessage.split("\"")[1];
				
				int methodpos = playerPage.indexOf("function " + newSigMethod);
				String newDecryptionMethod = playerPage.substring(methodpos);
				newDecryptionMethod = newDecryptionMethod.split("}")[0] + "};";
//				System.out.println(newDecryptionMethod);
				
				try {
					se.eval(decryptionMethod + newDecryptionMethod);
//					System.out.println(newSigMethod + " - " + decryptionMethod);
					String decryptedSig = (String) inv.invokeFunction(sigMethod, sig);
					return decryptedSig;
				} catch (Exception e2) {	
					e.printStackTrace();
				}
				
			}
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
		ArrayList<MediaStream> vidArray = new ArrayList<MediaStream>();

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
			MediaStream tempvidStream = new MediaStream(tempurl, tempquality, tempitag, tempsig, "video;");
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
	
	public static String getFmtList(String youtubePage) {
		try {
			return youtubePage.split("adaptive_fmts\": \"")[1]
					.split("\"")[0];
		} catch (Exception e) {
			System.err.println("No fmt list matched ");
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
