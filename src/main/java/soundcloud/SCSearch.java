package soundcloud;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
            String page = IOUtils.toString(new URL(searchURL));
            return extractJSON(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<SCSong> extractJSON(String jsonPage) {

        ArrayList<SCSong> songArray = new ArrayList<SCSong>();
        JSONArray songs = new JSONObject(jsonPage).getJSONArray("collection");
        JSONObject song;
        for (int i = 0; i < songs.length(); i++) {
            song = songs.getJSONObject(i);
            if (song.getString("kind").equals("track")) {
                songArray.add(parseElement(song));
            } else {
                System.out.println("Not adding : " + song.getString("kind"));
            }
        }
        return songArray;
    }

    public static SCSong parseElement(JSONObject element) {

        System.out.println("Paring element: \n" + element.toString());

        int id;
        String permalink;
        String title;
        String username;
        String artworkURL;
        int duration;
        boolean downloadable;

        id = element.getInt("id");
        title = element.getString("title");
        permalink = element.getString("permalink");
        username = element.getJSONObject("user").getString("username");
        artworkURL = element.isNull("artwork_url") ? null : element.getString("artwork_url");
        duration = element.getInt("duration");
        downloadable = element.getBoolean("downloadable");
        return new SCSong(id, title, permalink, username, artworkURL, duration, downloadable);
    }

}
