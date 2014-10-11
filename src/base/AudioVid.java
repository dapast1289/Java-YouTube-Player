package base;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

	public synchronized String getMediaURL() {
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
	}

	public AudioVid(SearchVid sv, String mediaURL) {
		super(sv.id, sv.title);
		this.mediaURL = mediaURL;
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
			generateImageView();
			imageView = new ImageView();
			imageView.setFitHeight(IMAGE_VIEW_SIZE);
			imageView.setFitWidth(IMAGE_VIEW_SIZE);
			return box = new HBox(12, imageView, labels);
		}
		box = new HBox(12, labels);
		return box;
	}

	protected void generateImageView() {
		Task<Image> task = new Task<Image>() {

			@Override
			protected Image call() throws Exception {
				Image img = new Image(iconURL);
				return img;
			}
		};

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				imageView.setImage((Image) event.getSource().getValue());
			}
		});

		Thread icongenThread = new Thread(task);
		icongenThread.start();
		System.out.println("Thread for " + title + " started");

	}

	public void generateMediaURL() {
		System.out.println(Thread.currentThread().getName());
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
