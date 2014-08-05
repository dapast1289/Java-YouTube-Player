package gui;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import base.AudioVid;

public class SongList extends ListView<String> {

	ArrayList<AudioVid> songList;
	AudioPlayer audioPlayer = AudioPlayer.getInstance();

	public SongList() {
		super();
		
		getStyleClass().add("list");
		setPrefWidth(500);

		addMouseListener();
	}

	public void setSongs(ArrayList<AudioVid> songs) {
		songList = songs;
		getItems().clear();
		for (AudioVid audioVid : songs) {
			getItems().add(audioVid.getTitle());
		}
	}

	public ArrayList<AudioVid> getSongList() {
		return songList;
	}

	public int selInd() {
		return getSelectionModel().getSelectedIndex();
	}

	public void addMouseListener() {
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						audioPlayer.playSongs(songList, selInd());
					}
				}
			}
		});
	}

}
