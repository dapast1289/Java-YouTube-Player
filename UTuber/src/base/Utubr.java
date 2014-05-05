package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.net.ssl.HttpsURLConnection;

public class Utubr {

	public static void main(String[] args) {
		Search("r3hab");
		try {
			Scraper(new URL("https://www.youtube.com/watch?v=Ij9Yce-n1LQ"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static Media media;
	static MediaPlayer mediaPlayer;

	public static ArrayList<String[]> Search(String search) {
		try {
			ArrayList<String[]> array = new ArrayList<>();
			URL url = new URL("https://www.youtube.com/results?search_query="
					+ URLEncoder.encode(search, "UTF-8"));
			HttpsURLConnection urlConnection = (HttpsURLConnection) url
					.openConnection();
			urlConnection
					.addRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

			Pattern vidPattern = Pattern
					.compile("href=\"/watch\\?(.{11,15})\\\">(.{1,100}?)</a>");
			Matcher vidMatcher = null;

			String s;
			String title;
			String tubelinkAfter = "";
			while ((s = in.readLine()) != null) {
				vidMatcher = vidPattern.matcher(s);

				if (vidMatcher.find()) {
					tubelinkAfter = vidMatcher.group(1);
					title = vidMatcher.group(2);
					System.out.println("https://www.youtube.com/watch?"
							+ tubelinkAfter + "\n");
					if (!tubelinkAfter.isEmpty() && !title.isEmpty())
						try {
							array.add(new String[] {
									"https://www.youtube.com/watch?"
											+ tubelinkAfter,
									URLDecoder.decode(title, "UTF-8") });
						} catch (Exception e) {
							System.out.println("decode failed at " + title);
							array.add(new String[] {
									"https://www.youtube.com/watch?"
											+ tubelinkAfter, title });
						}

				}
			}
			return array;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static YVid Scraper(URL url) {
		ArrayList<String[]> array = new ArrayList<>();
		String title = null;
		try {
			URLConnection urlConnection = url.openConnection();

			BufferedWriter writer;
			writer = new BufferedWriter(new PrintWriter(new FileOutputStream(
					"site" + ".html")));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

			String s = "";
			Pattern gvidPattern = Pattern
					.compile("url=(http.{200,1000}?)(s=\\[A-Z0-9]{20,30}\\.[A-Z0-9]{20,30}\\W){0,1}(,|\\%3B).{0,100}\\W(s=\\w{20,30}\\.\\w{20,30}\\\\u0026){0,1}");
			Matcher gvidMatcher = gvidPattern.matcher(s);
			while ((s = in.readLine()) != null) {
				if (s.matches("<title>.*</title>.*")) {
					title = URLDecoder.decode(
							s.split("<title>")[1].split("</title")[0], "UTF-8");
				}

				gvidMatcher = gvidPattern.matcher(s);
				writer.append(s + "\n");

				String type = "";

				while (gvidMatcher.find()) {
					String url0;
					String url1;
					String urlFinal;
					System.out.println(gvidMatcher.groupCount());
					for (int i = 0; i < gvidMatcher.groupCount() + 1; i++) {
						System.out.println(gvidMatcher.group(i));
					}

					url0 = gvidMatcher.group(1);
					url1 = URLDecoder.decode(url0, "UTF-8");
					String[] urlParts = url1.split("com");
					if (urlParts.length == 5) {
						String urlHead = urlParts[0].split("url=")[1] + "com";
						// System.out.println("head: " + urlHead);
						String urlTail = urlParts[1].substring(0,
								urlParts[1].length() - 1);
						// urlTail = URLDecoder.decode(urlTail, "UTF-8");
						// urlTail = URLDecoder.decode(urlTail,
						// "UTF-8").replaceAll("\\\\u0026", "&");
						urlTail = urlTail.replaceAll("\\\\u0026", "&");
						// if (urlTail.contains("type")) {
						// type = urlTail.split("type")[1];
						// // System.out.println("Type: " + type);
						// }
						// if (urlTail.contains(",type")) {
						// urlTail = urlTail.replace(",type", "&type");
						// }
						// if (urlTail.contains(",init")) {
						// urlTail = urlTail.replace(",init", "&init");
						// }
						// if (urlTail.contains(",index")) {
						// urlTail = urlTail.replace(",index", "&index");
						// }
						// if (urlTail.contains(",quality")) {
						// urlTail = urlTail.replace(",quality", "&quality");
						// }
						// if (urlTail.contains("&sver")) {
						// urlTail = urlTail.split("&sver=[0-9]&")[0] +
						// urlTail.split("&sver=[0-9]&")[1];
						// }
						// if (urlTail.contains("itag=")) {
						// System.out.println("contains");
						// while
						// (urlTail.matches(".*itag=[0-9]{1,4}\\W.*itag=[0-9]{1,4}\\W.*"))
						// {
						// System.out.println(urlTail);
						// urlTail = (urlTail.replaceFirst("itag=[0-9]{1,4}\\W",
						// ""));
						// System.out.println("cut off  ");
						// }
						// }
						urlFinal = urlHead + urlTail;
						System.out.println(urlFinal);

						if (gvidMatcher.groupCount() == 2) {
							System.out.println(gvidMatcher.group(2));
						}

						array.add(new String[] { type, urlFinal });

					}

				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new YVid(array, title);
	}
}
