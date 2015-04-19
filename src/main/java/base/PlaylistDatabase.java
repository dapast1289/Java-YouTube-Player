package base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import soundcloud.SCSong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Jaap Heijligers on 3/26/15.
 */
public class PlaylistDatabase {
    public static void main(String[] args) {
//        new PlaylistDatabase().addPlaylist("katy perry", YT_API.search("katy perry", 10));
        System.out.println(new PlaylistDatabase().getPlaylists());
        new PlaylistDatabase().addToPlaylist("ka", YT_API.search("kabout wesley", 1).get(0));
    }

    Path playlistFile = Paths.get("./playlists.json");
    HashMap<String, List<Song>> playlistMap;

    private static PlaylistDatabase pldb;

    public static synchronized PlaylistDatabase getInstance() {
        if (pldb == null) {
            pldb = new PlaylistDatabase();
        }
        return pldb;
    }

    private PlaylistDatabase() {
        readDataBase();
    }

    private void readDataBase() {
        if (playlistMap != null) return;
        playlistMap = new HashMap<>();
        try {
            if (!Files.exists(playlistFile)) {
                Files.createFile(playlistFile);
                return;
            }
            JSONObject dataBase = new JSONObject(new String(Files.readAllBytes(playlistFile)));
            System.out.println(dataBase.toString());
            Set<String> keys = dataBase.keySet();
            JSONObject obj;
            JSONArray playlist;
            for (String key: keys) {
                System.out.println("Key: " + key);
                playlist = dataBase.getJSONArray(key);
                List<Song> songs = new ArrayList<>();
                for (int i = 0; i < playlist.length(); i++) {
                    obj = playlist.getJSONObject(i);
                    if (obj.has("downloadable")) {
                        songs.add(new SCSong(obj));
                    } else {
                        songs.add(new AudioVid(obj));
                    }
                }
                playlistMap.put(key, songs);
            }
        } catch (IOException | JSONException e) {
            playlistMap = new HashMap<>();
        }
    }

    private void writeDatabase() {
        try {
            JSONObject obj = new JSONObject();
            for (String s : playlistMap.keySet()) {
                obj.put(s, makeList(playlistMap.get(s)));
            }

            Files.write(playlistFile, obj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray makeList(List<Song> playlist) {
        JSONArray array = new JSONArray();
        for (Song song : playlist) {
            array.put(song.toJSONObject());
        }
        return array;
    }

    public boolean hasPlaylist(String key) {
        return playlistMap.get(key) != null;
    }

    public List<Song> getPlaylist(String key) {
        return playlistMap.get(key);
    }

    public void addPlaylist(String key, List<Song> playlist) {
        playlistMap.put(key, playlist);
        writeDatabase();
    }

    public void addToPlaylist(String playlistName, Song vid) {
        if (playlistMap.get(playlistName) != null) {
            playlistMap.get(playlistName).add(vid);
            writeDatabase();
        } else {
            System.out.println("Tried to add to nonexistent playlist " + playlistName);
        }
    }

    private JSONArray listToJSON(List<? extends Song> list) {
        JSONArray array = new JSONArray();
        for (Song song : list) {
            array.put(song.toJSONObject());
        }
        return array;
    }

    public Set<String> getPlaylists() {
        return playlistMap.keySet();
    }

    public void removePlaylist(String name) {
        playlistMap.remove(name);
        writeDatabase();
    }

    public void removeFromPlaylist(String playlistName, Song song) {
        playlistMap.get(playlistName).remove(song);
        writeDatabase();
    }
//
//    public void removeFromPlaylist(String playlistName, int song) {
//        playlistMap.get(playlistName).remove(song);
//        writeDatabase();
//    }

}
