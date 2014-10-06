package base;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Radio extends Application {

	TextField searchField;
	SearchVid current;
	MediaView mediaView;
	MediaPlayer mediaPlayer;
	Media media;

	@Override
	public void start(Stage primaryStage) throws MalformedURLException,
			URISyntaxException {

		Label descLabel = new Label("Start radio with song or artist:");

		searchField = new TextField();
		searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER
						&& !searchField.getText().isEmpty()) {
					current = YT_API.search(searchField.getText(), 1).get(0);
					System.out.println(current);
					String stream = Extractor.extract(current).getSmall();
					System.out.println(stream);

					media = new Media(stream);
					mediaPlayer = new MediaPlayer(media);
					mediaView.setMediaPlayer(mediaPlayer);
					mediaPlayer.play();
					System.out.println(mediaPlayer.getError());
					System.out.println(mediaPlayer.getMedia().getSource());
					System.out.println(media.getMetadata());
					System.out.println(media.getDuration());
					System.out.println("Error: " + media.getError());
				}
			}
		});
		media = new Media(
				"http://r16---sn-5hnezn7y.googlevideo.com/videoplayback?ipbits=0&mws=yes&sparams=id%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Csource%2Cupn%2Cexpire&source=youtube&upn=s0gGGJOlF3U&signature=A77C00D1E61D48C1D28ABDDCBEC6A5699337BC02.207E5AB9F9601AC83172A9FB4F3CC29B6FC65545&initcwndbps=1738000&key=yt6&ip=87.195.202.22&sver=3&expire=1403406000&mv=m&fexp=910125%2C914071%2C930008%2C934026%2C934030%2C935411%2C945030%2C947362&ms=au&id=o-ACSjAj1RTRDJVSyeS8LD73zlWn_tZFB-gKeHUGPDU0b3&itag=5&mt=1403383624&ratebypass=yes&title=ASDF+Movie+%281-7%29");

		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);

		mediaView = new MediaView(mediaPlayer);
		mediaView.setX(100);
		mediaView.setY(100);

		FlowPane root = new FlowPane(Orientation.VERTICAL);
		root.setAlignment(Pos.CENTER);
		root.setHgap(20);
		root.setVgap(20);
		root.getChildren().add(descLabel);
		root.getChildren().add(searchField);
		root.getChildren().add(mediaView);

		System.out.println(mediaPlayer.getError());
		System.out.println(mediaPlayer.getMedia().getSource());
		System.out.println(media.getMetadata());
		System.out.println(media.getDuration());
		System.out.println("Error: " + media.getError());
		
		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public void gogog(Stage primaryStage) {
		final URL resource = getClass().getResource("a.mp3");
	    final Media media = new Media(resource.toString());
	    final MediaPlayer mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.play();

	    primaryStage.setTitle("Audio Player 1");
	    primaryStage.setWidth(200);
	    primaryStage.setHeight(200);
	    primaryStage.show();
	}
}