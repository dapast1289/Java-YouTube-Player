package gui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {
	
	Main main = Main.getInstance();

	public TopBar() {
		super();
		
		getStyleClass().add("box");
		
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
					main.setCenter(new SearchPane(tf.getText()));
					main.clearMenuSelection();
				}
			}
		});
	}

}