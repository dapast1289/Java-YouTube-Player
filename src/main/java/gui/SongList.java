package gui;

import base.Song;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;

public class SongList extends ListView<Song> {

	ArrayList<? extends Song> songList;
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
		setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {

			public ListCell<Song> call(ListView<Song> p) {

				ListCell<Song> cell = new ListCell<Song>() {

					@Override
					protected void updateItem(Song t, boolean bln) {
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
	public void setSongs(ArrayList<? extends Song> songs) {
		songList = songs;
		getItems().clear();
		
//		String title;
		for (Song Song : songs) {
//			title = Song.getTitle();
//			title = trim(title, 60);
			getItems().add(Song);
		}
		current = -1;
	}

	public ArrayList<? extends Song> getSongList() {
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
