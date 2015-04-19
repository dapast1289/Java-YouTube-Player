package base;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SpotiCharts {

	public static void main(String[] args) {
		ArrayList<AudioVid> a = getMostShared();
		for (AudioVid audioVid : a) {
			System.out.println(audioVid.getTitle());
			System.out.println(audioVid.getIconURL() + "\n");
		}
		getMostStreamed();
	}

	public static ArrayList<AudioVid> getMostShared() {
		try {
			URL chartsURL = new URL(
					"http://charts.spotify.com/api/tracks/most_viral/nl/weekly/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<AudioVid> getMostStreamed() {
		try {
			URL chartsURL = new URL(
					"http://charts.spotify.com/api/tracks/most_streamed/nl/daily/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<AudioVid> parseCharts(URL chartsURL) {
		ArrayList<AudioVid> array = new ArrayList<AudioVid>();

		try {
            String data = IOUtils.toString(chartsURL);
			JSONArray tracks = new JSONObject(data).getJSONArray("tracks");
			JSONObject track;
			String trackName, artistName, iconURL, name;

			for (int i = 0; i < tracks.length(); i++) {
				track = tracks.getJSONObject(i);
				trackName = track.getString("track_name");
				artistName = track.getString("artist_name");
				iconURL = track.getString("artwork_url");
				name = StringEscapeUtils.unescapeJson(artistName) + " - "
						+ StringEscapeUtils.unescapeJson(trackName);
				array.add(new AudioVid(null, name, null, iconURL));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

}
