package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import org.apache.commons.lang3.StringEscapeUtils;

public class Utubr {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "R3hab &amp; NERVO &amp; Ummet Ozcan - Revolution (Official Music Video)";
		s = StringEscapeUtils.unescapeHtml4(s);
		System.out.println(s);

		Utubr.Search("r3hab");
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
					// System.out.println("https://www.youtube.com/watch?"
					// + tubelinkAfter + "\n");
					if (!tubelinkAfter.isEmpty() && !title.isEmpty())

					array.add(new String[] {
							"https://www.youtube.com/watch?" + tubelinkAfter,
							StringEscapeUtils.unescapeHtml4(title) });
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
					title = StringEscapeUtils
							.unescapeHtml4(s.split("<title>")[1]
									.split("</title")[0]);
				}

				gvidMatcher = gvidPattern.matcher(s);
				writer.append(s + "\n");

				String type = "";

				while (gvidMatcher.find()) {
					String url0;
					String url1;
					String urlFinal;
					// System.out.println(gvidMatcher.groupCount());
					for (int i = 0; i < gvidMatcher.groupCount() + 1; i++) {
						// System.out.println(gvidMatcher.group(i));
					}

					url0 = gvidMatcher.group(1);
					url1 = URLDecoder.decode(url0, "UTF-8");
					String[] urlParts = url1.split("com");
					if (urlParts.length == 5) {
						String urlHead = urlParts[0].split("url=")[1] + "com";
						// System.out.println("head: " + urlHead);
						String urlTail = urlParts[1].substring(0,
								urlParts[1].length() - 1);
						urlTail = urlTail.replaceAll("\\\\u0026", "&");
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
