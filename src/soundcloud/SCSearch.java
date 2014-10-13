package soundcloud;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.Extractor;

public class SCSearch {
	
	public static void main(String[] args) {
		ArrayList<SCSong> a = search("sound remedy");
		for (SCSong scSong : a) {
			System.out.println(scSong.getMediaURL());
		}
	}

	public static ArrayList<SCSong> search(String searchString) {
		try {
			String searchURL = "http://api.soundcloud.com/search?q=" + URLEncoder.encode(searchString, "UTF-8") + "&client_id="
					+ SoundcloudExtract.CLIENT_ID;
			System.out.println("Search url : \n " + searchURL);
			String page = Extractor.httpToString(new URL(searchURL));
			return extractJSON(page);
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<SCSong> extractJSON(String jsonPage) {

		ArrayList<SCSong> songArray = new ArrayList<SCSong>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode songs = mapper.readTree(jsonPage).get("collection");
			for (Iterator<JsonNode> it = songs.elements(); it.hasNext();) {
				JsonNode element = (JsonNode) it.next();
				if (element.get("kind").asText().equals("track")) {
					songArray.add(parseElement(element));
				} else {
					System.out.println("Not adding : " + element.get("kind").asText());
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return songArray;
	}

	public static SCSong parseElement(JsonNode element) {

		String id, permalink, title, username, artworkURL;
		int duration;
		boolean downloadable;

		id = element.get("id").asText();
		title = element.get("title").asText();
		permalink = element.get("permalink").asText();
		username = element.get("user").get("username").asText();
		artworkURL = element.get("artwork_url").asText();
		duration = element.get("duration").asInt();
		downloadable = element.get("downloadable").equals("true") ? true : false;
		SCSong returnsong = new SCSong(id, title, permalink, username, artworkURL, duration, downloadable);
		return returnsong;
	}

}
