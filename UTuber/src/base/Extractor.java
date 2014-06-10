package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@SuppressWarnings("restriction")
public class Extractor {

	final static String fmt = "A\", \"url_encoded_fmt_stream_map\": \"type=vi\"";

	static Long time;

	public static void main(String[] args) {
		System.out.println(downloadSearchAudio("Imagine dragons").toURI());
	}

	public static File downloadSearchAudio(String s) {
		SearchVid searched = YT_API.search(s, 1).get(0);
		VidPage vp = extractFmt(searched.url, searched.title);

		System.out.println("type: " + vp.getAudioMS().getType());
		File downloadedFile = download(vp.getAudioStream(), searched.title
				+ ".mp3");
		return downloadedFile;
	}

	public static File downloadSearchVideo(String s) {
		SearchVid searched = YT_API.search(s, 1).get(0);
		VidPage vp = extract(searched.url, searched.title);
		System.out.println(vp.toString());
		System.out.println("type: " + vp.getSmallMS().getType());
		String name = searched.title + ".mp4";
		File downloadedFile = download(vp.getSmall(), name);
		return downloadedFile;
	}

	public static void stopwatch() {
		if (time == null) {
			time = System.currentTimeMillis();
			return;
		}
		System.out.println(System.currentTimeMillis() - time);
		time = null;
	}

	public static File download(String urlString, String filename) {
		try {
			URL url = new URL(urlString);
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			new File("download").mkdir();
			File file = new File("download/" + filename);
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

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
				type = string.substring(5).split("\\%3B")[0]
						.replace("%2F", ";");
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
		// System.out.println("returnStream= " + returnStream.toString());
		return returnStream;
	}

	public static String decryptSignature(String sig, String playerURLString) {

		System.out.println("decrypting: " + sig + " - " + playerURLString);

		if (sig == null) {
			return null;
		}

//		System.out.println(playerURLString);

		String playerID = playerURLString.substring(playerURLString
				.lastIndexOf('/') + 1);

//		System.out.println("PlayerID: " + playerID);

		File cacheFile = new File("cache/" + playerID);

		ScriptEngine se = null;
		Invocable inv = null;
		String actualMethod = "";
		String methodName = "";

		if (cacheFile.exists()) {
			System.out.println("cache found: " + playerID);
			String cacheMethod = "";
			try {
				BufferedReader fr = new BufferedReader(
						new FileReader(cacheFile));
				cacheMethod = fr.readLine();
				methodName = fr.readLine();
				fr.close();
				se = new ScriptEngineManager().getEngineByName("javascript");
				inv = (Invocable) se;
				se.eval(cacheMethod);
				System.out.println(methodName + " in " + cacheMethod);
				String decryptedSig = (String) inv.invokeFunction(methodName,
						sig);
				return decryptedSig;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		System.out.println("cache not found:" + playerID);

		String playerPage = "";
		try {
//			System.out.println("signature: " + sig);
			URL pURL = new URL(playerURLString);
			playerPage = urlToString(pURL);
			int methodNamePostion = playerPage.indexOf("signature=") + 10;
			methodName = playerPage.substring(methodNamePostion,
					methodNamePostion + 2);
//			System.out.println("Signature method name= " + methodName);

			int methodPostion = playerPage.indexOf("function " + methodName);
			actualMethod = playerPage.substring(methodPostion);
			actualMethod = actualMethod.split("}")[0] + "};";
//			System.out.println(actualMethod);

			se = new ScriptEngineManager().getEngineByName("javascript");
			inv = (Invocable) se;
			se.eval(actualMethod);
//			System.out.println(methodName + " - " + actualMethod);
			String decryptedSig = (String) inv.invokeFunction(methodName, sig);
			writeMethod(actualMethod, methodName, playerID);
//			System.out.println("returning: " + decryptedSig);
			return decryptedSig;

		} catch (Exception e) {
			String errormessage = e.getMessage();
			if (errormessage.contains("ReferenceError")) {
				String newMethodName = errormessage.split("\"")[1];
//				System.out.println("trying moar for " + newMethodName);
				
				String newActualMethod = getMethodFromPage(newMethodName, playerPage);
//				System.out.println(newActualMethod);

				try {
					actualMethod = actualMethod + newActualMethod;
					se.eval(actualMethod + newActualMethod);
//					System.out.println(newMethodName + " - " + actualMethod);
					String decryptedSig = (String) inv.invokeFunction(
							methodName, sig);
					writeMethod(actualMethod, methodName, playerID);
					return decryptedSig;
				} catch (Exception e2) {
					e.printStackTrace();
				}

			}
		}

		return null;
	}
	
	public static String getMethodFromPage(String methodName, String playerPage) {
		int methodPostion = playerPage.indexOf("function " + methodName);
		String actualMethod = playerPage.substring(methodPostion);
		actualMethod = actualMethod.split("}")[0] + "};";
		return actualMethod;
	}

	public static void writeMethod(String actualMethod, String methodName,
			String playerID) {
		File cacheFolder = new File("cache");
		File cacheFile = new File("cache/" + playerID);
		try {
			cacheFolder.mkdir();
			cacheFile.createNewFile();
			FileWriter fw = new FileWriter(cacheFile);
			fw.write(actualMethod + "\n" + methodName);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						if (!tempurl.contains("ratebypass")) {
							tempurl += "&ratebypass=yes";
						}
						if (!tempurl.contains("&title=")) {
							tempurl += "&title="
									+ URLEncoder.encode(title, "UTF-8");
						}

						// System.out.println(tempurl);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else if (string.contains("quality=")) {
					tempquality = string.split("quality=")[1];
				} else if (string.matches("itag=[0-9]{2,3}")) {
					tempitag = Integer.parseInt(string.substring(5));
				}
			}
			MediaStream tempvidStream = new MediaStream(tempurl, tempquality,
					tempitag, tempsig, "video;");
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
			return youtubePage.split("adaptive_fmts\": \"")[1].split("\"")[0];
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
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
