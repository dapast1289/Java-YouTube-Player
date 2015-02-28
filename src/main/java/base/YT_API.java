package base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class YT_API {

	public static void main(String[] args) {
		ArrayList<AudioVid> s = search("charlie the unicorn", 5);
		for (AudioVid audioVid : s) {
			System.out.println(audioVid.title);
			System.out.println(audioVid.id);
			System.out.println(audioVid.getIconURL());
		}
	}

	final static String YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title,thumbnails(medium)))&key=AIzaSyAaijMU4vmkUSE5tYd3pfCnBLwjsExqzPc&maxResults=:NUMBER_OF_ITEMS:&type=video&videoCategoryId=10&q=";
	final static String YOUTUBE_RELATED = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title,thumbnails(medium)))&key=AIzaSyAaijMU4vmkUSE5tYd3pfCnBLwjsExqzPc&maxResults=:NUMBER_OF_ITEMS:&type=video&videoCategoryId=10&relatedToVideoId=";

	public static ArrayList<AudioVid> search(String search, int numberOfItems) {
		return parseAPIJson(getSearchUrl(search, numberOfItems));
	}

	public static ArrayList<AudioVid> getRelated(String videoID,
			int numberOfItems) {
		try {
			return parseAPIJson(getRelatedURL(videoID, numberOfItems));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getURL(String search) {
		ArrayList<AudioVid> list = search(search, 1);
		if (list.size() == 0)
			return null;
		
		VidPage page = Extractor.extract(list.get(0));
		return page.getDecodedStream(0);
	}

	private static ArrayList<AudioVid> parseAPIJson(URL url) {
		ArrayList<AudioVid> searchArray = new ArrayList<AudioVid>();
		String title, id, iconURL;

		String searchResultJSON = Extractor.httpToString(url);

		ObjectMapper mapper = new ObjectMapper();
		try {

			JsonNode items = mapper.readTree(searchResultJSON).get("items");
			JsonNode vidItem;

			for (int i = 0; i < items.size(); i++) {
				vidItem = items.get(i);

				id = vidItem.get("id").get("videoId").asText();
				title = vidItem.get("snippet").get("title").asText();
				iconURL = vidItem.get("snippet").get("thumbnails")
						.get("medium").get("url").asText();
				searchArray.add(new AudioVid(id, title, null, iconURL));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return searchArray;
	}

	private static URL getRelatedURL(String videoID, int numberOfItems)
			throws MalformedURLException {
		if (videoID.length() != 11) {
			System.err.println("Wrong URL length");
			throw new MalformedURLException();
		}
		try {
			return new URL(YOUTUBE_RELATED.replace(":NUMBER_OF_ITEMS:",
					String.valueOf(numberOfItems))
					+ URLEncoder.encode(videoID, "UTF-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static URL getSearchUrl(String search, int numberOfItems) {
		try {
			return new URL(YOUTUBE_SEARCH_URL.replace(":NUMBER_OF_ITEMS:",
					String.valueOf(numberOfItems))
					+ URLEncoder.encode(search, "UTF-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
