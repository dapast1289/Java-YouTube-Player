package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	static BorderPane root;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new BorderPane();
		
		MenuList list = new MenuList();
		AudioPlayer ap = new AudioPlayer();
		TopBar topBar = new TopBar();

		root.setLeft(list);
		root.setBottom(ap);
		root.setTop(topBar);

		Scene scene = new Scene(root, 1200, 800);
		scene.getStylesheets().add("style.css");

		primaryStage.setTitle("Utubr");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			public void handle(WindowEvent event) {
				AudioPlayer.stop();
				Platform.exit();
			}
		});
	}

	public static void setCenter(Node n) {
		root.setCenter(n);
	}
}
