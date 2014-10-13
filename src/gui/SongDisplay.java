package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import base.Song;

public class SongDisplay extends VBox{
	
	private ImageView imageView;
	private Label songLabel;
	private Label artistLabel;
	private String song;
	private String artist;
	private String title;
	private static SongDisplay songDisplay;
	

	public SongDisplay() {
		super(5);
		songDisplay = this;
		getStyleClass().add("songdisplay");
		imageView = new ImageView();
		imageView.setFitWidth(240);
		imageView.setPreserveRatio(true);
		songLabel = new Label("-");
		songLabel.setAlignment(Pos.BASELINE_CENTER);
		songLabel.getStyleClass().add("song");
		songLabel.setFont(Font.font("arial", FontWeight.BOLD, 16));
		songLabel.setWrapText(true);
		artistLabel = new Label("-");
		artistLabel.setAlignment(Pos.BASELINE_CENTER);
		artistLabel.getStyleClass().add("artist");
		artistLabel.setFont(Font.font("arial", FontWeight.NORMAL, 14));
		artistLabel.setWrapText(true);
		
		setAlignment(Pos.BASELINE_CENTER);
		
		getChildren().add(songLabel);
		getChildren().add(artistLabel);
		getChildren().add(imageView);
	}
	
	public void setSong(Song av) {
		title = av.getTitle();
		if (title.contains(" - ")) {
			artist = title.split(" - ")[0];
			artist = trim(artist);
			song = title.split(" - ")[1];
			song = trim(song);
		} else {
			song = title;
			artist = "";
		}
		songLabel.setText(song);
		artistLabel.setText(artist);
		imageView.setImage(av.getImageView().getImage());
	}
	
	public static String trim(String s) {
		if (s.length() > 30) {
			return s.substring(0,30) + "\n" + s.substring(30);
		}
		return s;
	}
	
	public static SongDisplay getInstance() {
		if (songDisplay == null) {
			songDisplay = new SongDisplay();
		}
		return songDisplay;
	}

}
