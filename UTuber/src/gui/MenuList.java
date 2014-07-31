package gui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MenuList extends ListView<String> {

	public final String TOP_LISTS = "Top Lists";
	public final String RADIO = "Radio";

	TopLists toplists;

	public MenuList() {
		super();

		ArrayList<String> menuElements = new ArrayList<String>();
		menuElements.add(TOP_LISTS);
		menuElements.add(RADIO);

		ObservableList<String> observableList = FXCollections
				.observableList(menuElements);
		setItems(observableList);

		getSelectionModel().selectFirst();

		toplists = new TopLists();

		getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String oldValue, String newValue) {
						if (oldValue != newValue) {
							if (newValue.equals(TOP_LISTS)) {
								Main.setCenter(toplists);
							} else {
								Main.setCenter(new HBox());
							}
						}
					}
				});
		
		Platform.runLater(new Runnable() {

			public void run() {
				Main.setCenter(toplists);

			}
		});
	}

}
