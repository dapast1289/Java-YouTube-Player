package gui;

import base.PlaylistDatabase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private BorderPane root;
	private Scene scene;
	private Stage stage;
	private MenuList menuList;
	private AudioPlayer audioPlayer;
	private TopBar topBar;
	private static Main main;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		main = this;
		stage = primaryStage;
		
		root = new BorderPane();
		
		audioPlayer = new AudioPlayer();
		menuList = new MenuList();
		topBar = new TopBar();

		root.setLeft(menuList);
		root.setBottom(audioPlayer);
		root.setTop(topBar);

		scene = new Scene(root, 1200, 800);
		scene.getStylesheets().add("/style.css");

		stage.setTitle("Utubr");
		stage.setScene(scene);
		stage.show();

        PlaylistDatabase.getInstance();

		stage.setOnCloseRequest(event -> {
            audioPlayer.stop();
            Platform.exit();
            System.exit(0);
        });
	}
	
	public void clearMenuSelection() {
		menuList.getItemList().getSelectionModel().clearSelection();
	}

	public void setCenter(Node n) {
		root.setCenter(n);
	}
	
	public void setTitle(String title) {
		stage.setTitle("UTubr | " + title);
	}
	
	public static Main getInstance() {
		return main;
	}
}
