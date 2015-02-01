package base;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AudioVid extends SearchVid implements Song {
    String mediaURL;
    String iconURL;
    SongCellBox box;

    public synchronized String getMediaURL() {
        if (!hasMediaURL()) {
            generateMediaURL();
        }
        return mediaURL;
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
        System.out.println(Thread.currentThread().getName());
        if (mediaURL != null) {
            return;
        }

        if (url == null)
            generateURL();

        try {
            mediaURL = Extractor.extractFmt(this).getAudioStream();
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


}
