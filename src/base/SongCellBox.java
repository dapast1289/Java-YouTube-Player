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

public class SongCellBox extends HBox {
	Label artistLabel;
	Label titleLabel;
	VBox labels;
	String iconURL;
	ImageView imageView;
	public final static int IMAGE_VIEW_SIZE = 60;

	public SongCellBox(String title, String iconURL) {
		super();
		this.iconURL = iconURL;
		artistLabel = new Label(title.split("-")[0].trim());
		artistLabel.setFont(Font.font("Arial", 20));
		artistLabel.setTextFill(Paint.valueOf("#CCC"));

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
			imageView = new ImageView();
			generateImageView();
			imageView.setFitHeight(IMAGE_VIEW_SIZE);
			imageView.setFitWidth(IMAGE_VIEW_SIZE);
			getChildren().add(imageView);
		}
		getChildren().add(labels);
		setSpacing(12);
	}

	public void generateImageView() {
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
	}

	/**
	 * @return the imageView
	 */
	public ImageView getImageView() {
		return imageView;
	}
	
	

}
