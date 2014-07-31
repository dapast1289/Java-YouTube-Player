package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javafx.scene.layout.VBox;

import javax.swing.JTextField;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import base.AudioVid;
import base.SearchVid;
import base.YT_API;

public class RadioPane extends VBox {

	static AudioMediaPlayerComponent vlc;
	static int current;
	static AudioVid currentSong;
	static ArrayList<AudioVid> songList;
	static MediaPlayer player;

	public RadioPane() {
		super();

	}

	public static void addEnterBind(JTextField jtf) {
		jtf.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String search = e.getActionCommand();
				SearchVid origin = YT_API.search(search, 1).get(0);
				ArrayList<AudioVid> list = YT_API.getRelated(origin.id, 50);
				songList = new ArrayList<AudioVid>();
				songList.add(new AudioVid(origin, null));
				for (SearchVid searchVid : list) {
					songList.add(new AudioVid(searchVid, null));
				}

				if (list.size() > 0) {
					current = 0;
					currentSong = songList.get(current);
					System.out.println("Searched: " + currentSong.getTitle());
					playSong(currentSong);

				} else {
					System.err.println("none found");
				}
			}
		});
	}

	public static void playNext() {
		current++;
		if (current >= songList.size()) {
			current = 0;
			System.err.println("over");
		}
		playSong(songList.get(current));
	}

	public static void playSong(AudioVid av) {
		player.playMedia(av.getMediaURL());
		System.out.println("Now playing: " + av.getMediaURL());
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
