package base;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class JRadio {

    public static void main(String[] args) {
	initJRadio();
    }

    static JFrame jf;
    static JTextField jtf;
    static AudioMediaPlayerComponent vlc;
    static SearchVid song;
    static SearchVid nextSong;
    static String nextSongMediaURL;
    static ArrayList<SearchVid> playedSongs;
    static MediaPlayer player;
    static JButton nextButton;
    static JLabel songLabel;

    public static void initJRadio() {
	playedSongs = new ArrayList<SearchVid>();

	initVLC();
	initLookAndFeel();

	// JFrame
	jf = new JFrame("Radio");
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.setPreferredSize(new Dimension(500, 400));
	jf.setLocationRelativeTo(null);
	jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.PAGE_AXIS));

	// JTextField
	jtf = new JTextField();
	jtf.setFont(new Font("Arial", Font.BOLD, 18));
	jtf.setMaximumSize(new Dimension(300, 40));
	addEnterBind(jtf);

	vlc = new AudioMediaPlayerComponent();
	player = vlc.getMediaPlayer();

	nextButton = new JButton("Next");
	nextButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		playNext();
	    }
	});
	
	songLabel = new JLabel("Enter a song");
	songLabel.setFont(new Font("Arial", Font.BOLD, 20));
	
	jtf.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	nextButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	songLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	
	jf.add(Box.createVerticalStrut(50));
	jf.add(jtf);
	jf.add(Box.createVerticalStrut(50));
	jf.add(nextButton);
	jf.add(Box.createVerticalStrut(50));
	jf.add(songLabel);

	jf.pack();
	jf.setVisible(true);
    }

    public static void initVLC() {
	NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
		"C:/Program Files/VideoLAN/VLC");
	Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }

    public static void initLookAndFeel() {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}
    }

    public static void addEnterBind(JTextField jtf) {
	jtf.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		String search = e.getActionCommand();
		ArrayList<SearchVid> list = YT_API.search(search, 1);
		if (list.size() > 0) {
		    song = list.get(0);
		    System.out.println("Searched: " + song.getTitle());
		    String mediaURL = Extractor.extractFmt(song)
			    .getAudioStream();
		    playSong(mediaURL, song.title);

		    playedSongs.add(song);

		    generateNextSong();

		} else {
		    System.err.println("none found");
		}
	    }
	});
    }

    public static void generateNextSong() {
	if (song == null) {
	    System.err.println("nothing playing, not generating next song");
	    return;
	}

	nextSongMediaURL = null;
	nextSong = null;

	ArrayList<SearchVid> songList = YT_API.getRelated(song.id, 50);
	for (SearchVid searchVid : songList) {
	    if (!hasPlayed(searchVid)) {
		nextSong = searchVid;
		nextSongMediaURL = Extractor.extractFmt(nextSong)
			.getAudioStream();
		System.out.println("generated " + nextSong.title);
		return;
	    }
	}

	System.out.println("phase 2");

	if (nextSong == null) {
	    for (SearchVid searchVid : songList) {
		ArrayList<SearchVid> secondList = YT_API.getRelated(
			searchVid.id, 50);
		for (SearchVid searchVid2 : secondList) {
		    if (!hasPlayed(searchVid2)) {
			nextSong = searchVid2;
			nextSongMediaURL = Extractor.extractFmt(nextSong)
				.getAudioStream();
			System.out.println("generated " + nextSong.title);
			return;
		    }
		}
	    }
	}
	System.err.println("none found");
	playedSongs.clear();
	generateNextSong();
    }

    public static boolean hasPlayed(SearchVid sv) {
	String id = sv.id;
	for (SearchVid searchVid : playedSongs) {
	    if (id.equals(searchVid.id)) {
		return true;
	    }
	}
	return false;
    }

    public static void playNext() {
	if (nextSong == null || nextSongMediaURL == null) {
	    System.err.println("next song is null");
	    generateNextSong();
	}
	playedSongs.add(song);
	song = nextSong;
	
	playSong(nextSongMediaURL, nextSong.title);
	SwingUtilities.invokeLater(new Runnable() {
	    
	    public void run() {
		generateNextSong();
	    }
	});
    }
    
    public static void playSong(String mediaURL, String title) {
	player.playMedia(mediaURL);
	songLabel.setText(title);
    }

    public static void onFinish(MediaPlayer mp) {
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

	    }

	    public void buffering(MediaPlayer mediaPlayer, float newCache) {

	    }

	    public void backward(MediaPlayer mediaPlayer) {

	    }
	});
    }

}
