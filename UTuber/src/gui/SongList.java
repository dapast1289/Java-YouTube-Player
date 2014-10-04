package gui;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import base.AudioVid;

public class SongList extends ListView<AudioVid> {

	ArrayList<AudioVid> songList;
	AudioPlayer audioPlayer = AudioPlayer.getInstance();
	int current;

	public SongList() {
		super();
		
		getStyleClass().add("songlist");
		setPrefWidth(500);

		initSongList();
		addMouseListener();
		
	}
	protected void initSongList() {
		setCellFactory(new Callback<ListView<AudioVid>, ListCell<AudioVid>>() {

			public ListCell<AudioVid> call(ListView<AudioVid> p) {

				ListCell<AudioVid> cell = new ListCell<AudioVid>() {

					@Override
					protected void updateItem(AudioVid t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
//							setText(t.getApp().getAppName());
							setGraphic(t.getBox());
						}
					}
				};
				return cell;
			}
		});
	}
	public void setSongs(ArrayList<AudioVid> songs) {
		songList = songs;
		getItems().clear();
		
//		String title;
		for (AudioVid audioVid : songs) {
//			title = audioVid.getTitle();
//			title = trim(title, 60);
			getItems().add(audioVid);
		}
		current = -1;
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
