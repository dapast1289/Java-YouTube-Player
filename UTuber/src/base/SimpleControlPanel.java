package base;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class SimpleControlPanel extends JPanel {

    private static final long serialVersionUID = 6659194932504554526L;
    private final EmbeddedMediaPlayer mediaPlayer;
    private JSlider positionSlider;
    private final ScheduledExecutorService executorService = Executors
	    .newSingleThreadScheduledExecutor();
    private boolean isSlidingPositionSlider = false;
    private JButton downloadButton;
    private JButton mp3downloadButton;
    private JButton playButton;

    public static void main(String[] args) {
	new GUI_Main();
    }

    public SimpleControlPanel(EmbeddedMediaPlayer mp) {
	super();
	mediaPlayer = mp;
	positionSlider = new JSlider(0, 10000);
	executorService.scheduleAtFixedRate(new Updater(mediaPlayer), 0L, 50L,
		TimeUnit.MILLISECONDS);

	setLayout(new FlowLayout());

	downloadButton = new JButton("Download");
	downloadButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		Extractor.download(mediaPlayer.getMediaMeta().getTitle(),
			SidePanel.getSelectedName() + ".mp4");
	    }
	});

	mp3downloadButton = new JButton("Download mp3");
	mp3downloadButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		String dl = SidePanel.getSelectedAudioMp4Preffered();
		System.out.println("Downloading mp4: " + dl);
		File mp4File = Extractor.download(dl,
			SidePanel.getSelectedName() + ".mp4");
		execute(new String[] { "avconv", "-i",
			mp4File.getAbsolutePath(),
			SidePanel.getSelectedName() + ".mp3" });
	    }
	});

	playButton = new JButton("Play/Pause");
	playButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		if (mediaPlayer.isPlaying()) {
		    mediaPlayer.pause();
		} else {
		    mediaPlayer.start();
		}

	    }
	});

	JButton nextButton = new JButton("+10");
	nextButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		mediaPlayer.setPosition((float) (mediaPlayer.getPosition() + 0.1));
	    }
	});

	add(playButton);
	add(positionSlider);
	add(downloadButton);
	add(mp3downloadButton);
	add(nextButton);

	positionSlider.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		isSlidingPositionSlider = true;
		mediaPlayer.pause();
		setSliderBasedPosition();
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		isSlidingPositionSlider = false;
		setSliderBasedPosition();
		mediaPlayer.start();
	    }
	});
    }

    public static void openWebpage(URI uri) {
	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
		: null;
	if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	    try {
		desktop.browse(uri);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    private void setSliderBasedPosition() {
	if (!mediaPlayer.isSeekable()) {
	    return;
	}
	float positionValue = (float) positionSlider.getValue() / 10000.0f;
	// Avoid end of file freeze-up
	if (positionValue > 0.999f) {
	    positionValue = 0.999f;
	}

	mediaPlayer.setPosition(positionValue);
    }

    final class Updater implements Runnable {

	EmbeddedMediaPlayer player;

	public Updater(EmbeddedMediaPlayer player) {
	    this.player = player;
	}

	public void run() {
	    int position = (int) (player.getPosition() * 10000f);
	    if (!isSlidingPositionSlider)
		positionSlider.setValue(position);
	}

    }

    public static ArrayList<String> execute(String[] commands) {
	System.out.println("commands: " + Arrays.deepToString(commands));
	ProcessBuilder pb = new ProcessBuilder(commands);
	ArrayList<String> array = new ArrayList<String>();
	try {
	    Process proc = pb.start();
	    BufferedReader input = new BufferedReader(new InputStreamReader(
		    proc.getInputStream()));
	    BufferedReader error = new BufferedReader(new InputStreamReader(
		    proc.getErrorStream()));
	    String s = null;

	    while ((s = input.readLine()) != null) {
		System.out.println(s);

		array.add(s);
	    }

	    while ((s = error.readLine()) != null) {
		System.out.println("e: " + s);
	    }

	} catch (IOException e) {
	    System.out.println("command not found");

	}

	return array;
    }
}