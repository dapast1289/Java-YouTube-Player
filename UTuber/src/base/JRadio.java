package base;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

    final static int SLIDER_SCALE = 1000000;

    static JFrame jf;
    static JTextField jtf;
    static JSlider seeker;
    static boolean isScrolling = false;

    static AudioMediaPlayerComponent vlc;
    static int current;
    static AudioVid currentSong;
    static ArrayList<AudioVid> songList;
    static MediaPlayer player;
    static JButton nextButton;
    static JLabel songLabel;
    static Thread generatorThread;

    static JScrollPane listpane;
    static JList<String> jlist;
    static DefaultListModel<String> listModel;

    static ScheduledExecutorService executorService;

    public static void initJRadio() {
	songList = new ArrayList<AudioVid>();

	initVLC();
	initLookAndFeel();

	// JFrame
	jf = new JFrame("Radio");
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.setPreferredSize(new Dimension(600, 800));
	// jf.setLocationRelativeTo(null);
	jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.PAGE_AXIS));

	seeker = new JSlider(0, SLIDER_SCALE);
	addMouseListener(seeker);

	// JTextField
	jtf = new JTextField();
	jtf.setFont(new Font("Arial", Font.BOLD, 18));
	jtf.setMaximumSize(new Dimension(300, 40));
	addEnterBind(jtf);

	vlc = new AudioMediaPlayerComponent();
	player = vlc.getMediaPlayer();
	addMPListener(player);

	nextButton = new JButton("Next");
	nextButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		playNext();
	    }
	});

	songLabel = new JLabel("Enter a song");
	songLabel.setFont(new Font("Arial", Font.BOLD, 20));

	listModel = new DefaultListModel<String>();
	jlist = new JList<String>(listModel);
	listpane = new JScrollPane(jlist);
	initJListListener();

	jtf.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	nextButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	songLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

	jf.add(Box.createVerticalStrut(10));
	jf.add(jtf);
	jf.add(Box.createVerticalStrut(20));
	jf.add(nextButton);
	jf.add(seeker);
	jf.add(Box.createVerticalStrut(20));
	jf.add(songLabel);
	jf.add(Box.createVerticalStrut(20));
	jf.add(listpane);

	jf.pack();
	jf.setVisible(true);

	executorService = Executors.newSingleThreadScheduledExecutor();
	executorService.scheduleAtFixedRate(new Updater(player), 0L, 50L,
		TimeUnit.MILLISECONDS);
    }

    static class Updater implements Runnable {

	MediaPlayer mp;

	public Updater(MediaPlayer mp) {
	    this.mp = mp;
	}

	public void run() {
	    if (!isScrolling)
		seeker.setValue((int) (mp.getPosition() * 1000000));
	}

    }

    public static void addMouseListener(JSlider js) {
	js.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		isScrolling = true;
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		player.setPosition((float) seeker.getValue() / SLIDER_SCALE);
		isScrolling = false;
	    }
	});
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
		SearchVid origin = YT_API.search(search, 1).get(0);
		ArrayList<SearchVid> list = YT_API.getRelated(origin.id, 50);
		songList = new ArrayList<AudioVid>();
		songList.add(new AudioVid(origin, null));
		for (SearchVid searchVid : list) {
		    songList.add(new AudioVid(searchVid, null));
		}

		updateSongList();
		if (list.size() > 0) {
		    current = 0;
		    currentSong = songList.get(current);
		    System.out.println("Searched: " + currentSong.getTitle());
		    playSong(currentSong);

		    generateSongs();

		} else {
		    System.err.println("none found");
		}
	    }
	});
    }

    public static void updateSongList() {
	listModel.clear();
	for (AudioVid audioVid : songList) {
	    listModel.addElement(audioVid.title);
	}
    }

    public static void generateSongs() {
	if (generatorThread != null && generatorThread.isAlive()) {
	    System.out.println("Killing Thread");
	    generatorThread.interrupt();
	}
	generatorThread = new Thread(new Runnable() {

	    public void run() {
		for (AudioVid audioVid : songList) {
		    try {
			audioVid.generateMediaURL();
		    } catch (Exception e) {
			e.printStackTrace();
			songList.remove(audioVid);
			updateSongList();
		    }
		    System.out.println(audioVid.title);
		    if (Thread.interrupted()) {
			System.out.println("Thread killed");
			break;
		    }
		}

	    }
	});
	generatorThread.start();
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
	songLabel.setText(av.getTitle());
	jlist.setSelectedIndex(current);
	System.err.println("Lenght: " + player.getLength());
    }

    public static void initJListListener() {
	jlist.addListSelectionListener(new ListSelectionListener() {

	    public void valueChanged(ListSelectionEvent e) {

		SwingUtilities.invokeLater(new Runnable() {

		    public void run() {
			int selectedIndex = jlist.getSelectedIndex();
			if (selectedIndex != current) {
			    current = selectedIndex;
			    playSong(songList.get(selectedIndex));
			}
		    }
		});

	    }
	});
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
