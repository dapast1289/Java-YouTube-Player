package gui;

import javafx.scene.layout.HBox;
import base.Charter;

public class TopLists extends HBox {

	public TopLists() {
		super();
		SongList left = new SongList();
		left.setSongs(Charter.getMostStreamed());
		left.setPrefWidth(10000);

		SongList right = new SongList();
		right.setSongs(Charter.getMostShared());
		right.setPrefWidth(10000);
		
		setMaxWidth(10000);
		
		getChildren().add(left);
		getChildren().add(right);
	}

}
