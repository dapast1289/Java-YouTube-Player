package base;

import java.io.File;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class jfx extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Media media = new Media(new File("/home/jaap/git/JYT/UTuber/weapon.mp3").toURI().toASCIIString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();

    primaryStage.setTitle("Audio Player 1");
    primaryStage.setWidth(200);
    primaryStage.setHeight(200);
    primaryStage.show();
  }
}

   