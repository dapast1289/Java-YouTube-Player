package gui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuList extends VBox {

	public final String TOP_LISTS = "Top Lists";
	public final String RADIO = "Radio";

	private TopLists toplists;
	private RadioPane radioPane;
	private ListView<String> itemList;
	private Main main = Main.getInstance();
	private SongDisplay songDisplay = SongDisplay.getInstance();

	public MenuList() {
		super();
		getStyleClass().add("list");

		ArrayList<String> menuElements = new ArrayList<String>();
		menuElements.add(TOP_LISTS);
		menuElements.add(RADIO);

		ObservableList<String> observableList = FXCollections
				.observableList(menuElements);
		itemList = new ListView<String>(observableList);

		itemList.getSelectionModel().selectFirst();

		toplists = new TopLists();
		radioPane = new RadioPane();

		itemList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> arg0,
							String oldValue, String newValue) {
						System.out.println("Changed from " + oldValue + " to "
								+ newValue);
						if (newValue.equals(TOP_LISTS)) {
							main.setCenter(toplists);
						} else if (newValue.equals(RADIO)) {
							main.setCenter(radioPane);
						} else {
							main.setCenter(new HBox());
						}
					}
				});
		
		itemList.setPrefHeight(1000);

		getChildren().add(itemList);
		getChildren().add(songDisplay);

		Platform.runLater(new Runnable() {

			public void run() {
				main.setCenter(toplists);

			}
		});
	}

	public ListView<String> getItemList() {
		return itemList;
	}

	public void setItemList(ListView<String> itemList) {
		this.itemList = itemList;
	}
}
