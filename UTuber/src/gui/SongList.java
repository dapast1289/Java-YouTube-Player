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
	int current;
//	String currentTitle;

	public SongList() {
		super();
		
		getStyleClass().add("list");
		setPrefWidth(500);

		addMouseListener();
		
	}

	public void setSongs(ArrayList<AudioVid> songs) {
		songList = songs;
		getItems().clear();
		
		String title;
		for (AudioVid audioVid : songs) {
			title = audioVid.getTitle();
			title = trim(title, 60);
			getItems().add(title);
		}
		current = -1;
//		currentTitle = null;
	}
	
	public static String trim(String s, int n) {
		if (s.length() > n) {
			s = s.substring(0, n) + "…";
		}
		return s;
	}

	public ArrayList<AudioVid> getSongList() {
		return songList;
	}

	public int selInd() {
		return getSelectionModel().getSelectedIndex();
	}
	
	public void setCurrent(int i) {
		if (i >= getItems().size()) {
			setCurrent(0);
			return;
		}
		System.out.println("Next : " + i);
		if ( i >= 0) {
			getSelectionModel().select(i);
		}
		current = i;
	}
	
	public void next() {
		setCurrent(current + 1);
	}
	
	public void clearCurrent() {
		getSelectionModel().clearSelection();
		current = -1;
	}

	public void addMouseListener() {
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 1) {
						int i = selInd();
						if (i == current) {
							return;
						}
						audioPlayer.playSongs((SongList) mouseEvent.getSource(), i);
						setCurrent(i);
					}
				}
			}
		});
	}

}
