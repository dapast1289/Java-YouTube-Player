package base;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class BetterControlPanel extends JPanel {

	private static final long serialVersionUID = 6659194932504554526L;
	private final EmbeddedMediaPlayer mediaPlayer;
	private JSlider positionSlider;
	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();
	private boolean isSlidingPositionSlider = false;
	private JButton downloadButton;
	private JButton playButton;

	
	public static void main(String[] args) {
		new GUI_Main();
	}
	
	public BetterControlPanel(EmbeddedMediaPlayer mp) {
		super();
		mediaPlayer = mp;
		positionSlider = new JSlider(0, 10000);
		executorService.scheduleAtFixedRate(new Updater(mediaPlayer), 0L, 50L,
				TimeUnit.MILLISECONDS);
		
		setLayout(new FlowLayout());
		
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openWebpage(new URI(mediaPlayer.getMediaMeta().getTitle()));
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		playButton = new JButton("Play/Pause");
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
				} else {
					mediaPlayer.start();
				}
				
			}
		});
		
		add(playButton);
		add(positionSlider);
		add(downloadButton);
		
		
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
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
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

		@Override
		public void run() {
			int position = (int) (player.getPosition() * 10000f);
			if (!isSlidingPositionSlider)
			positionSlider.setValue(position);
		}

	}
}