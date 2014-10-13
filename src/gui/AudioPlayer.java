package gui;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import base.AudioVid;
import base.Song;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class AudioPlayer extends HBox {

	private AudioMediaPlayerComponent vlc;
	private MediaPlayer player;
	private ArrayList<? extends Song> songArray;
	private SongList songList;
	private int current;
	private Song currentSong;
	private Main main = Main.getInstance();
	private Button nextButton;
	private Button playPauseButton;
	// private Button openInVlcButton;
	private Slider slider;
	private boolean isDragging = false;
	private final ScheduledExecutorService sliderUpdater = Executors.newSingleThreadScheduledExecutor();
	private SongDisplay songDisplay = SongDisplay.getInstance();
	static private AudioPlayer audioPlayer;

	public static AudioPlayer getInstance() {
		return audioPlayer;
	}

	public AudioPlayer() {
		super();
		audioPlayer = this;
		getStyleClass().addAll("box", "bar");

		setPrefHeight(50);
		setAlignment(Pos.CENTER);

		initVLC();

		vlc = new AudioMediaPlayerComponent();
		player = vlc.getMediaPlayer();
		addMPListener(player);

		nextButton = new Button("▶▶");
		nextButton.setPrefWidth(50);
		nextButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				playNext();
			}
		});

		playPauseButton = new Button("▐▐ ");
		playPauseButton.setPrefWidth(50);
		playPauseButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				Button source = (Button) event.getSource();
				if (player.isPlaying()) {
					player.pause();
					source.setText("▶");
				} else {
					if (player.isPlayable()) {
						player.play();
						source.setText("▐▐ ");
					}
				}
			}
		});
		//
		// openInVlcButton = new Button("Open in vlc");
		// openInVlcButton.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent arg0) {
		// String stream = Extractor.extract(currentSong).getDecodedStream(0);
		// try {
		// player.pause();
		// System.out.println("Opening stream: " + stream);
		// Runtime.getRuntime().exec("vlc " + stream);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// });

		slider = new Slider(0f, 1f, 0f);
		sliderUpdater.scheduleAtFixedRate(new Updater(player), 0L, 50L, TimeUnit.MILLISECONDS);

		slider.setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				System.out.println("mouse pressed");
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
		setHgrow(slider, Priority.ALWAYS);

		getChildren().add(playPauseButton);
		getChildren().add(nextButton);
		// getChildren().add(openInVlcButton);
		getChildren().add(slider);
	}

	private void initVLC() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "classes/VLC");
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");
		try {
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void playSongs(SongList sl, int index) {
		if (songList != null) {
			songList.clearCurrent();
		}
		songList = sl;
		songArray = sl.getSongList();
		System.out.println(songArray.get(index));
		System.out.println();
		playSong(songArray.get(index));
		current = index;
		sl.setCurrent(current);
	}

	private void playSong(Song av) {
		currentSong = av;
		if (!av.hasMediaURL()) {
			System.out.println("Starting thread");
			startMediaThread();
		} else {
			System.out.println("Not starting thread");
			player.playMedia(av.getMediaURL());
		}
		
		songDisplay.setSong(currentSong);
		main.setTitle(currentSong.getTitle());

		System.out.println("Now playing: " + av.getTitle());

	}
	
	protected void startMediaThread() {
		Task<String> mediaTask = new Task<String>() {

			@Override
			protected String call() throws Exception {
				System.out.println("Generating song: " + currentSong.hasMediaURL());
				String mediaURL = currentSong.getMediaURL();
				return mediaURL;
			}
		};

		mediaTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						player.playMedia(currentSong.getMediaURL());
					}
				}).start();
			}
		});
		
		Thread mediaThread = new Thread(mediaTask);
		mediaThread.start();
		
	}

	public void playNext() {
		songList.next();
		current++;
		if (current >= songArray.size()) {
			current = 0;
		}
		System.out.println("Playing next: " + songArray.get(current).getTitle());
		playSong(songArray.get(current));
	}

	public void stop() {
		player.stop();
	}

	private void addMPListener(MediaPlayer mp) {
		mp.addMediaPlayerEventListener(new MediaPlayerEventListener() {

			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {

			}

			public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {

			}

			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {

			}

			public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {

			}

			public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
				System.out.println("Sub finished");

			}

			public void stopped(MediaPlayer mediaPlayer) {
				System.out.println("Stopped");

			}

			public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {

			}

			public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {

			}

			public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {

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

			public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {

			}

			public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {

			}

			public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {

			}

			public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {

			}

			public void mediaFreed(MediaPlayer mediaPlayer) {

			}

			public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {

			}

			public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {

			}

			public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {

			}

			public void forward(MediaPlayer mediaPlayer) {

			}

			public void finished(MediaPlayer mediaPlayer) {
				System.out.println("Finished");
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						playNext();
					}
				});
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
