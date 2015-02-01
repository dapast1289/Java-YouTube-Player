package base;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItunesCharts {
	
	final static String topSongsURL = "https://itunes.apple.com/us/rss/topsongs/limit=100/xml";
//	final static String newReleasesURL = "https://itunes.apple.com/WebObjects/MZStore.woa/wpa/MRSS/newreleases/sf=143441/limit=100/rss.xml";
	
	public static void main(String[] args) {
		ArrayList<AudioVid> list = getTopSongs();
		for (AudioVid audioVid : list) {
			System.out.println(audioVid.title);
		}
	}

	public static ArrayList<AudioVid> getTopSongs() {
		return parseCharts(topSongsURL);
	}

	//	public static ArrayList<AudioVid> getNewSongs() {
//		return parseCharts(newReleasesURL);
//	}
	
	protected static ArrayList<AudioVid> parseCharts(String url) {
		try {
			ArrayList<AudioVid> array = new ArrayList<AudioVid>();
			Document doc = Jsoup.connect(url).get();
			Elements entries = doc.getElementsByTag("entry");
			String title, iconURL;
			for (Element element : entries) {
				title = element.getElementsByTag("title").text();
				title = title.split(" - ")[1] + " - " + title.split(" - ")[0];
				iconURL = element.getElementsByAttributeValue("height", "170").text();
				array.add(new AudioVid(null, title, null, iconURL));
			}
			
			return array;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
