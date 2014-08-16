package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import base.AudioVid;

public class SongDisplay extends VBox{
	
	private AudioVid audioVid;
	private ImageView imageView;
	private Label songLabel;
	private Label artistLabel;
	private String song;
	private String artist;
	private String title;
	private static SongDisplay songDisplay;
	

	public SongDisplay() {
		super();
		setMinHeight(200);
		songDisplay = this;
		getStyleClass().add("songdisplay");
		imageView = new ImageView();
		songLabel = new Label("-");
		songLabel.setAlignment(Pos.BASELINE_CENTER);
		songLabel.getStyleClass().add("song");
		songLabel.setFont(Font.font("arial", FontWeight.BOLD, 16));
		artistLabel = new Label("-");
		artistLabel.setAlignment(Pos.BASELINE_CENTER);
		artistLabel.getStyleClass().add("artist");
		artistLabel.setFont(Font.font("arial", FontWeight.NORMAL, 14));
		
		setAlignment(Pos.BASELINE_CENTER);
		
		getChildren().add(songLabel);
		getChildren().add(artistLabel);
	}
	
	public void setAudioVid(AudioVid av) {
		title = av.getTitle();
		if (title.contains(" - ")) {
			artist = title.split(" - ")[0];
			song = title.split(" - ")[1];
		} else {
			song = title;
			artist = "";
		}
		songLabel.setText(song);
		artistLabel.setText(artist);
	}
	
	public static SongDisplay getInstance() {
		if (songDisplay == null) {
			songDisplay = new SongDisplay();
		}
		return songDisplay;
	}

}
