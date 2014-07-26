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
	    System.out.println(searchVid.title);
	}

    }

    final static String YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title))&key=AIzaSyAaijMU4vmkUSE5tYd3pfCnBLwjsExqzPc&maxResults=:NUMBER_OF_ITEMS:&type=video&videoCategoryId=10&q=";
    final static String YOUTUBE_RELATED = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title))&key=AIzaSyAaijMU4vmkUSE5tYd3pfCnBLwjsExqzPc&maxResults=:NUMBER_OF_ITEMS:&type=video&videoCategoryId=10&relatedToVideoId=";

    public static ArrayList<SearchVid> search(String search, int numberOfItems) {
	return parseAPIJson(getSearchUrl(search, numberOfItems));
    }

    public static ArrayList<SearchVid> getRelated(String videoID,
	    int numberOfItems) {
	try {
	    return parseAPIJson(getRelatedURL(videoID, numberOfItems));
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static ArrayList<SearchVid> parseAPIJson(URL url) {
	ArrayList<SearchVid> searchArray = new ArrayList<SearchVid>();

	String title = null;
	String id = null;

	String searchResultJSON = Extractor.urlToString(url);
	String[] parts = searchResultJSON.split("\"id\": \\{");

	Pattern idPattern = Pattern.compile("\"videoId\": \"(.*)\"\\s+\\}");
	Matcher idMatcher = null;
	Pattern titlePattern = Pattern.compile("\"title\": \"(.*)\"\\s+\\}");
	Matcher titleMatcher = null;

	for (String string : parts) {
	    idMatcher = idPattern.matcher(string);
	    if (idMatcher.find()) {
		id = idMatcher.group(1);
		titleMatcher = titlePattern.matcher(string);
		if (titleMatcher.find()) {
		    title = titleMatcher.group(1).replaceAll("\\\\\"", "\"");

		    searchArray.add(new SearchVid(id, title));
		}
	    }
	}
	return searchArray;
    }

    public static URL getRelatedURL(String videoID, int numberOfItems)
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

    public static URL getSearchUrl(String search, int numberOfItems) {
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
