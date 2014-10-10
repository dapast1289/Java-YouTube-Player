package base;

import java.io.IOException;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItunesCharts {
	
	final static String url = "https://itunes.apple.com/us/rss/topsongs/limit=100/xml";
	
	public static void main(String[] args) {
		new JFXPanel();
		getTopSongs();
	}

	public static ArrayList<AudioVid> getTopSongs() {
		try {
			ArrayList<AudioVid> array = new ArrayList<AudioVid>();
			Document doc = Jsoup.connect(url).get();
			Elements entries = doc.getElementsByTag("entry");
			String title, iconURL;
			for (Element element : entries) {
				title = element.getElementsByTag("title").text();
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
