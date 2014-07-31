package gui;

import java.util.ArrayList;

import javafx.scene.control.Label;
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

	static AudioMediaPlayerComponent vlc;
	static MediaPlayer player;
	static ArrayList<AudioVid> songList;
	static int current;
	static AudioVid currentSong;
	static Label songLabel;

	public AudioPlayer() {
		super();
		getStyleClass().add("hbox");

		setPrefHeight(50);

		initVLC();

		vlc = new AudioMediaPlayerComponent();
		player = vlc.getMediaPlayer();
		addMPListener(player);

		songLabel = new Label("Playing song");
		songLabel.getStyleClass().add("text");

		getChildren().add(songLabel);
	}

	public static void initVLC() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				"C:/Program Files/VideoLAN/VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	}

	public static void playSongs(ArrayList<AudioVid> songs, int index) {
		songList = songs;
		playSong(songList.get(index));
		current = index;
	}

	public static void playSong(AudioVid av) {
		currentSong = songList.get(current);
		player.playMedia(av.getMediaURL());
		songLabel.setText(av.getTitle());
		System.out.println("Now playing: " + av.getMediaURL());
	}

	public static void playNext() {
		current++;
		if (current >= songList.size()) {
			current = 0;
		}
		playSong(currentSong);
	}

	public static void stop() {
		player.stop();
	}

	public static void addMPListener(MediaPlayer mp) {
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

			}

			public void stopped(MediaPlayer mediaPlayer) {

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

}
