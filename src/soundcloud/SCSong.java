package soundcloud;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import base.Song;
import base.SongCellBox;

public class SCSong implements Song {

	String id, title, permalink, username, iconURL, mediaURL = null;
	int duration;
	boolean downloadable;
	SongCellBox box;

	public SCSong(String id, String title, String permalink, String username, String artworkURL, int duration,
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

	public String getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	
}
