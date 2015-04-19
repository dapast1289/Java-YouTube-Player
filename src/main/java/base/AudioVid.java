package base;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

public class AudioVid extends SearchVid implements Song {
    String mediaURL;
    String iconURL;
    SongCellBox box;

    public static void main(String[] args) {
        AudioVid vid = new AudioVid(null, "Love me like you do", null, null);
        System.out.println(vid.getMediaURL());
    }

    public synchronized String getMediaURL() {
        if (!hasMediaURL()) {
            generateMediaURL();
        }
        return mediaURL;
    }

    public AudioVid(JSONObject obj) {
        this(obj.has("id") ? obj.getString("id") : null,
                obj.has("title") ? obj.getString("title") : null,
                null,
                obj.has("iconURL") ? obj.getString("iconURL") : null);
    }

    public AudioVid(String id, String title, String mediaURL, String iconURL) {
        super(id, title);
        this.mediaURL = mediaURL;
        this.iconURL = iconURL;
    }

    public AudioVid(SearchVid sv, String mediaURL) {
        super(sv.id, sv.title);
        this.mediaURL = mediaURL;
    }

    public HBox getBox() {
        if (box != null) {
            return box;
        }
        box = new SongCellBox(title, iconURL);
        return box;
    }

    public void generateMediaURL() {
        if (mediaURL != null) {
            return;
        }

        if (url == null)
            generateURL();

        try {
            mediaURL = VideoExtractor.getInstance().extractAudio(url);
            System.out.println(title + " - " + url + " - " + mediaURL);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(title + "\n" + url);
        }
    }

    protected void generateURL() {
        if (url != null) {
            return;
        }

        AudioVid tmp = YT_API.search(title, 1).get(0);
        url = tmp.url;
        title = tmp.title;
    }

    public boolean hasMediaURL() {
        return mediaURL != null;
    }

    public String getIconURL() {
        return iconURL;
    }

    /**
     * @return the imageView
     */
    public ImageView getImageView() {
        getBox();
        if (box == null) {
            return new ImageView();
        }
        return box.getImageView();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.putOpt("id", id);
        obj.putOpt("title", title);
        obj.putOpt("iconURL", iconURL);
        return obj;
    }


}
