package soundcloud;

import base.Song;
import base.SongCellBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

public class SCSong implements Song {

    String title, permalink, username, iconURL, mediaURL = null;
    int duration, id;
    boolean downloadable;
    SongCellBox box;

    public SCSong(JSONObject obj) {
        this(obj.getInt("id"), obj.getString("title"), obj.getString("permalink"), obj.getString("username"),
                obj.getString("artworkURL"), obj.getInt("duration"), obj.getBoolean("downloadable"));
    }

    public SCSong(int id, String title, String permalink, String username, String artworkURL, int duration,
                  boolean downloadable) {
        super();
        this.id = id;
        this.title = title;
        this.permalink = permalink;
        this.username = username;
        this.iconURL = artworkURL;
        this.duration = duration;
        this.downloadable = downloadable;
    }

    @Override
    public boolean hasMediaURL() {
        return mediaURL != null;
    }

    @Override
    public String getMediaURL() {
        if (!hasMediaURL()) {
            generateMediaURL();
        }
        return mediaURL;
    }

    @Override
    public void generateMediaURL() {
        if (mediaURL != null) {
            return;
        }
        if (downloadable) {
            mediaURL = "https://api.soundcloud.com/tracks/" + id + "/download?client_id=" + SoundcloudExtract.CLIENT_ID;
        } else {
            mediaURL = SoundcloudExtract.parseStreamJSONdlURL(this);
        }
    }

    @Override
    public String getIconURL() {
        return iconURL;
    }

    @Override
    public HBox getBox() {
        if (box != null) {
            return box;
        }
        box = new SongCellBox(title, iconURL);
        return box;
    }

    @Override
    public ImageView getImageView() {
        return box.getImageView();
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public int getId() {
        return id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.putOpt("id", id);
        obj.putOpt("title", title);
        obj.putOpt("permalink", permalink);
        obj.putOpt("username", username);
        obj.putOpt("iconURL", iconURL);
        obj.putOpt("duration", duration);
        obj.putOpt("downloadable", downloadable);
        return obj;
    }


}
