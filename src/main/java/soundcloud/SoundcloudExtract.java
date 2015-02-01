package soundcloud;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.Extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SoundcloudExtract {

	final static String CLIENT_ID = "b45b1aa10f1ac2941910a7f0d10f8e28";
	final static String IPHONE_ID = "376f225bf427445fc4bfb6b99b72e0bf";
	final static Pattern songPattern = Pattern
			.compile("https?://(www\\.)?soundcloud.com/(?<artist>.*?)/(?<permalink>.*?)$");

	public static void main(String[] args) {
		
	}

	public static SCSong retrieveSong(String scURL) {
		Matcher m = songPattern.matcher(scURL);
		if (m.find()) {
			String permalink = m.group("permalink");
			String artist = m.group("artist");
			String infoURL = resolveURL(artist, permalink);
			System.out.println(infoURL);
			return parseInfoJSON(infoURL);
		} else {
			throw new NullPointerException("Wrong URL supplied");
		}
	}

	protected static String parseStreamJSONdlURL(SCSong song) {
		try {
			String streamJsonURL = "http://api.soundcloud.com/i1/tracks/"
					+ song.getId() + "/streams?client_id=" + CLIENT_ID
					+ "&secret_token=None";

			String jsonPage = Extractor.httpToString(new URL(streamJsonURL));

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonPage);
			if (root.has("http_mp3_128_url")) {
				return root.get("http_mp3_128_url").asText();
			} else if (root.has("hls_mp3_128_url")) {
				return root.get("hls_mp3_128_url").asText();
			} else if (root.has("preview_mp3_128_url")) {
				return root.get("preview_mp3_128_url").asText();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String resolveURL(String artist, String title) {
		String url = "https://soundcloud.com/" + artist + "/" + title;
		String resolveURL = "http://api.soundcloud.com/resolve.json?url=" + url
				+ "&client_id=" + CLIENT_ID;
		return resolveURL;
	}

	protected static SCSong parseInfoJSON(String infoURL) {
		String id, permalink, title, username, artworkURL;
		int duration;
		boolean downloadable;
		try {
			String jsonPage = Extractor.httpToString(new URL(infoURL));
			System.out.println(jsonPage);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonPage);
			id = root.get("id").asText();
			title = root.get("title").asText();
			permalink = root.get("permalink").asText();
			username = root.get("user").get("username").asText();
			artworkURL = root.get("artwork_url").asText();
			duration = root.get("duration").asInt();
			downloadable = root.get("downloadable").equals("true") ? true
					: false;
			return new SCSong(id, title, permalink, username, artworkURL,
					duration, downloadable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
