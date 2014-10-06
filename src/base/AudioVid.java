package base;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AudioVid extends SearchVid {
	String mediaURL;
	String iconURL;
	HBox box;
	ImageView imageView;

	public final static int IMAGE_VIEW_SIZE = 60;

	public String getMediaURL() {
		if (!hasMediaURL()) {
			generateMediaURL();
		}
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public AudioVid(String id, String title, String mediaURL, String iconURL) {
		super(id, title);
		this.mediaURL = mediaURL;
		this.iconURL = iconURL;
		getBox();
	}

	public AudioVid(SearchVid sv, String mediaURL) {
		super(sv.id, sv.title);
		this.mediaURL = mediaURL;
		getBox();
	}

	public HBox getBox() {
		if (box != null) {
			return box;
		}
		Label artistLabel = new Label(title.split("-")[0].trim());
		artistLabel.setFont(Font.font("Arial", 20));
		artistLabel.setTextFill(Paint.valueOf("#CCC"));
		Label titleLabel;
		VBox labels;
		try {
			titleLabel = new Label(title.split("-")[1].trim());
			titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
			titleLabel.setTextFill(Paint.valueOf("#EEE"));
			labels = new VBox(5, titleLabel, artistLabel);
		} catch (Exception e) {
			titleLabel = new Label(title.split("-")[0].trim());
			titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
			titleLabel.setTextFill(Paint.valueOf("#EEE"));
			labels = new VBox(5, titleLabel);
		}
		if (!(iconURL == null)) {
			imageView = new ImageView(iconURL);
			imageView.setFitHeight(IMAGE_VIEW_SIZE);
			imageView.setFitWidth(IMAGE_VIEW_SIZE);
			return box = new HBox(12, imageView, labels);
		}
		box = new HBox(12, labels);
		return box;
	}

	public void generateMediaURL() {
		if (mediaURL != null) {
			return;
		}

		generateURL();

		try {
			mediaURL = Extractor.extractFmt(this).getAudioStream();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(title + "\n" + url);
		}
	}

	private void generateURL() {
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

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	/**
	 * @return the imageView
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * @param imageView
	 *            the imageView to set
	 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

}
