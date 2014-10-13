package gui;

import soundcloud.SCSearch;
import base.YT_API;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {
	
	Main main = Main.getInstance();
	final protected double SFSize = 200;

	public TopBar() {
		super();
		
		getStyleClass().addAll("box", "bar");
		
		TextField youtubeSearchField = new TextField();
		youtubeSearchField.setPromptText("Search");
		youtubeSearchField.setPrefWidth(SFSize);
		addYTOnEnter(youtubeSearchField);

		getChildren().add(youtubeSearchField);
		
		TextField soundcloudSearchField = new TextField();
		soundcloudSearchField.setPromptText("Search");
		soundcloudSearchField.setPrefWidth(SFSize);
		addSCOnEnter(soundcloudSearchField);

		getChildren().add(soundcloudSearchField);
		
		
	}

	public void addYTOnEnter(final TextField tf) {
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER && !tf.getText().isEmpty()) {
					System.out.println("Searching: " + tf.getText());
					SongList songList = new SongList();
					songList.setSongs(YT_API.search(tf.getText(), 50));
					main.setCenter(songList);
					main.clearMenuSelection();
				}
			}
		});
	}
	
	public void addSCOnEnter(final TextField tf) {
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER && !tf.getText().isEmpty()) {
					System.out.println("Searching: " + tf.getText());
					SongList songList = new SongList();
					songList.setSongs(SCSearch.search(tf.getText()));
					main.setCenter(songList);
					main.clearMenuSelection();
				}
			}
		});
	}

}
