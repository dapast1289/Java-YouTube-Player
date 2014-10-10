package gui;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import base.ItunesCharts;

public class TopLists extends HBox {
	SongList left;
	SongList right;

	public TopLists() {
		super();
		left = new SongList();
		left.setPrefWidth(10000);

		right = new SongList();
		right.setPrefWidth(10000);

		setMaxWidth(10000);

		getChildren().add(left);
		getChildren().add(right);
		
		
		Platform.runLater(new Runnable() {
			
			public void run() {
				setSongs();
			}
		});
	}

	public void setSongs() {
		Platform.runLater(new Runnable() {

			public void run() {
				left.setSongs(ItunesCharts.getTopSongs());
//				right.setSongs(Charter.getMostStreamed());
			}
		});

	}

}
