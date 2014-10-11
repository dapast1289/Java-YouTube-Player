package base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
					"http://charts.spotify.com/api/tracks/most_viral/global/weekly/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<AudioVid> getMostStreamed() {
		try {
			URL chartsURL = new URL(
					"http://charts.spotify.com/api/tracks/most_streamed/global/daily/latest");
			return parseCharts(chartsURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<AudioVid> parseCharts(URL chartsURL) {
		ArrayList<AudioVid> array = new ArrayList<AudioVid>();
		String data = Extractor.httpToString(chartsURL);
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode tracks = mapper.readTree(data).get("tracks");
			JsonNode track;
			String trackName, artistName, iconURL, name;

			for (int i = 0; i < tracks.size(); i++) {
				track = tracks.get(i);
				trackName = track.get("track_name").asText();
				artistName = track.get("artist_name").asText();
				iconURL = track.get("artwork_url").asText();
				name = StringEscapeUtils.unescapeJson(artistName) + " - "
						+ StringEscapeUtils.unescapeJson(trackName);
				array.add(new AudioVid(null, name, null, iconURL));
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

}
