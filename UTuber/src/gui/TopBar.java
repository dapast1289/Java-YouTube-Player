package gui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {

	public TopBar() {
		super();
		
		getStyleClass().add("hbox");
		
		TextField searchField = new TextField();
		searchField.setPromptText("Search");
		addOnEnter(searchField);

		getChildren().add(searchField);
	}

	public void addOnEnter(final TextField tf) {
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER && !tf.getText().isEmpty()) {
					System.out.println("Searching: " + tf.getText());
					Main.setCenter(new SearchPane(tf.getText()));
				}
			}
		});
	}

}
