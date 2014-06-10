package base;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YT_API {
	
	public static void main(String[] args) {
		ArrayList<SearchVid> s = search("charlie the unicorn", 5);
		for (SearchVid searchVid : s) {
			System.out.println(searchVid.url);
		}
		System.out.println(s.size());
		
	}

	final static String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title))&key=AIzaSyAaijMU4vmkUSE5tYd3pfCnBLwjsExqzPc&maxResults=:NUMBER_OF_ITEMS:&type=video&q=";
	
	
	public static ArrayList<SearchVid> search(String search, int numberOfItems) {
		
		ArrayList<SearchVid> searchArray = new ArrayList<SearchVid>();
		
		String title = null;
		String id = null;
		
		String searchResultJSON = Extractor.urlToString(getUrl(search, numberOfItems));
		
		String[] parts = searchResultJSON.split("\"id\": \\{");
		
		Pattern idPattern = Pattern.compile("\"videoId\": \"(.*)\"\\s+\\}");
		Matcher idMatcher = null;
		Pattern titlePattern = Pattern.compile("\"title\": \"(.*)\"\\s+\\}");
		Matcher titleMatcher = null;
		
		for (String string : parts) {
			idMatcher = idPattern.matcher(string);
			if (idMatcher.find()) {
				id = idMatcher.group(1);
				System.out.println("id: " + id);
				
				titleMatcher = titlePattern.matcher(string);
				if (titleMatcher.find()) {
					title = titleMatcher.group(1).replaceAll("\\\\\"", "\"");
					System.out.println("title: " + title);
					
					searchArray.add(new SearchVid(id, title));
				}
			}
		}
		return searchArray;
	}
	
	public static URL getUrl(String search, int numberOfItems) {
		try {
			return new URL(YOUTUBE_API_URL.replace(":NUMBER_OF_ITEMS:", String.valueOf(numberOfItems)) + URLEncoder.encode(search, "UTF-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}

