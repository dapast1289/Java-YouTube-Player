package gui;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import base.AudioVid;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class AudioPlayer extends HBox {

	private AudioMediaPlayerComponent vlc;
	private MediaPlayer player;
	private ArrayList<AudioVid> songList;
	private int current;
	private AudioVid currentSong;
	private Label songLabel;
	private Main main = Main.getInstance();
	private Button nextButton;
	private Slider slider;
	private boolean isDragging = false;
	private final ScheduledExecutorService sliderUpdater = Executors
			.newSingleThreadScheduledExecutor();
	static private AudioPlayer audioPlayer;

	public static AudioPlayer getInstance() {
		return audioPlayer;
	}

	public AudioPlayer() {
		super();
		audioPlayer = this;
		getStyleClass().add("box");

		setPrefHeight(50);

		initVLC();

		vlc = new AudioMediaPlayerComponent();
		player = vlc.getMediaPlayer();
		addMPListener(player);

		songLabel = new Label("Playing song");
		songLabel.getStyleClass().add("text");

		nextButton = new Button("Next");
		nextButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				playNext();
			}
		});

		slider = new Slider(0f, 1f, 0f);
		sliderUpdater.scheduleAtFixedRate(new Updater(player), 0L, 50L,
				TimeUnit.MILLISECONDS);

		slider.setOnDragDetected(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				System.out.println("drag detected");
				isDragging = true;
			}
		});

		slider.setOnMouseReleased(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				System.out.println("mouse released");
				player.setPosition((float) slider.getValue());
				isDragging = false;
			}
		});

		getChildren().add(nextButton);
		getChildren().add(slider);
		getChildren().add(songLabel);
	}

	public void initVLC() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				"C:/Program Files/VideoLAN/VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	}

	public void playSongs(ArrayList<AudioVid> songs, int index) {
		songList = songs;
		playSong(songList.get(index));
		current = index;
	}

	public void playSong(AudioVid av) {
		songLabel.setText(av.getTitle());
		System.err.println("Songlabel set");
		main.setTitle(av.getTitle());
		currentSong = av;
		player.playMedia(currentSong.getMediaURL());
		System.out.println("Now playing: " + av.getMediaURL());
	}

	public void playNext() {
		current++;
		if (current >= songList.size()) {
			current = 0;
		}
		System.out.println("Playing next: " + songList.get(current).getTitle());
		playSong(songList.get(current));
	}

	public void stop() {
		player.stop();
	}

	public void addMPListener(MediaPlayer mp) {
		mp.addMediaPlayerEventListener(new MediaPlayerEventListener() {

			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {

			}

			public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {

			}

			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {

			}

			public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {

			}

			public void subItemFinished(MediaPlayer mediaPlayer,
					int subItemIndex) {
				System.out.println("Sub finished");

			}

			public void stopped(MediaPlayer mediaPlayer) {
				System.out.println("Stopped");

			}

			public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {

			}

			public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {

			}

			public void positionChanged(MediaPlayer mediaPlayer,
					float newPosition) {

			}

			public void playing(MediaPlayer mediaPlayer) {

			}

			public void paused(MediaPlayer mediaPlayer) {

			}

			public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {

			}

			public void opening(MediaPlayer mediaPlayer) {

			}

			public void newMedia(MediaPlayer mediaPlayer) {

			}

			public void mediaSubItemAdded(MediaPlayer mediaPlayer,
					libvlc_media_t subItem) {

			}

			public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {

			}

			public void mediaParsedChanged(MediaPlayer mediaPlayer,
					int newStatus) {

			}

			public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {

			}

			public void mediaFreed(MediaPlayer mediaPlayer) {

			}

			public void mediaDurationChanged(MediaPlayer mediaPlayer,
					long newDuration) {

			}

			public void mediaChanged(MediaPlayer mediaPlayer,
					libvlc_media_t media, String mrl) {

			}

			public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {

			}

			public void forward(MediaPlayer mediaPlayer) {

			}

			public void finished(MediaPlayer mediaPlayer) {
				System.out.println("Finished");
				playNext();
			}

			public void error(MediaPlayer mediaPlayer) {

			}

			public void endOfSubItems(MediaPlayer mediaPlayer) {
				playNext();

			}

			public void buffering(MediaPlayer mediaPlayer, float newCache) {

			}

			public void backward(MediaPlayer mediaPlayer) {

			}
		});
	}

	final class Updater implements Runnable {

		private MediaPlayer mPlayer;

		public Updater(MediaPlayer p) {
			this.mPlayer = p;
		}

		public void run() {
			Platform.runLater(new Runnable() {

				public void run() {
					if (!isDragging)
						slider.setValue(mPlayer.getPosition());
				}
			});
		}
	}

}
