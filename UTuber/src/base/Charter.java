package base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		parseCharts();
	}
	
	public static ArrayList<SearchVid> parseCharts() {
		try {
			ArrayList<SearchVid> array = new ArrayList<SearchVid>();
			URL chartsURL = new URL("http://charts.spotify.com/api/charts/most_shared/global/latest");
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
						
						name = StringEscapeUtils.unescapeJson(songArtist[0]) + " - " + StringEscapeUtils.unescapeJson(songArtist[1]);
						System.out.println("name: " + name);
						array.add(new SearchVid(null, name));
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
