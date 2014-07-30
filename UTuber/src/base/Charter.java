package base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import org.apache.commons.lang3.StringEscapeUtils;

public class Charter extends JScrollPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1013963011447988481L;

	public static void main(String[] args) {
		getMostShared();
		getMostStreamed();
	}
	

	public static ArrayList<AudioVid> getMostShared() {
		try {
			URL chartsURL = new URL("http://charts.spotify.com/api/charts/most_shared/global/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<AudioVid> getMostStreamed() {
		try {
			URL chartsURL = new URL("http://charts.spotify.com/api/charts/most_streamed/global/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<AudioVid> parseCharts(URL chartsURL) {
		try {
			ArrayList<AudioVid> array = new ArrayList<AudioVid>();
			URLConnection urlConnection = chartsURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String s = "";
			String name = "";
			String[] splitString;
			while ((s = in.readLine())!= null) {
				splitString = s.split("\\{");
				for (String string : splitString) {
					string = string.replaceAll(".*?track_name\"\\:\"(.*?)\".*artist_name\"\\:\"(.*?)\".*", "$1;$2");
					String[] songArtist = string.split(";");
					if (songArtist.length == 2) {
						name = StringEscapeUtils.unescapeJson(songArtist[1]) + " - " + StringEscapeUtils.unescapeJson(songArtist[0]);
						array.add(new AudioVid(null, name, null));
					}
				}
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
