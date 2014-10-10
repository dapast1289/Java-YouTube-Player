package gui;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {
	
	Main main = Main.getInstance();
	final protected double SFSize = 200;

	public TopBar() {
		super();
		
		getStyleClass().addAll("box", "bar");
		
		TextField searchField = new TextField();
		searchField.setPromptText("Search");
		searchField.setPrefWidth(SFSize);
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
