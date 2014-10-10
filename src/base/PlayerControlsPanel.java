package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerControlsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int SKIP_TIME_MS = 10 * 1000;

	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();

	private final EmbeddedMediaPlayer mediaPlayer;

	private JLabel timeLabel;
	// private JProgressBar positionProgressBar;
	private JSlider positionSlider;
	private JLabel chapterLabel;

	private JButton previousChapterButton;
	private JButton rewindButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton fastForwardButton;
	private JButton nextChapterButton;

	private JButton toggleMuteButton;
	private JSlider volumeSlider;

//	private JButton captureButton;
//
//	private JButton ejectButton;
//	private JButton connectButton;
//	private JButton subTitlesButton;

	private JFileChooser fileChooser;

	private boolean firstVolumeChanged;

	private boolean mousePressedPlaying = false;

	public PlayerControlsPanel(EmbeddedMediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;

		createUI();

		executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayer),
				0L, 1L, TimeUnit.SECONDS);
	}

	private void createUI() {
		createControls();
		layoutControls();
		registerListeners();
	}

	private void createControls() {
		
		firstVolumeChanged = false;
		timeLabel = new JLabel("hh:mm:ss");

		// positionProgressBar = new JProgressBar();
		// positionProgressBar.setMinimum(0);
		// positionProgressBar.setMaximum(1000);
		// positionProgressBar.setValue(0);
		// positionProgressBar.setToolTipText("Time");

		positionSlider = new JSlider();
		positionSlider.setMinimum(0);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		positionSlider.setToolTipText("Position");

		chapterLabel = new JLabel("00/00");

		previousChapterButton = new JButton("Previous");
		// previousChapterButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/previous.png")));
		previousChapterButton.setToolTipText("Go to previous chapter");

		rewindButton = new JButton("Rewind");
		// rewindButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/previous.png")));
		rewindButton.setToolTipText("Skip back");

		stopButton = new JButton("Stop");
		// stopButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/stop.png")));
		stopButton.setToolTipText("Stop");

		pauseButton = new JButton("Play/pause");
		// pauseButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/pause.png")));
		pauseButton.setToolTipText("Play/pause");

		playButton = new JButton("Play");
		// playButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/play.png")));
		playButton.setToolTipText("Play");

		fastForwardButton = new JButton("Skip");
		// fastForwardButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/next.png")));
		fastForwardButton.setToolTipText("Skip forward");

		nextChapterButton = new JButton("Next");
		// nextChapterButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/control_end_blue.png")));
		nextChapterButton.setToolTipText("Go to next chapter");

		toggleMuteButton = new JButton("Mute");
		// toggleMuteButton.setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("icons/sound_mute.png")));
		toggleMuteButton.setToolTipText("Toggle Mute");

		volumeSlider = new JSlider();
		volumeSlider.setOrientation(JSlider.HORIZONTAL);
		volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
		volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
		volumeSlider.setPreferredSize(new Dimension(100, 40));
		volumeSlider.setToolTipText("Change volume");
		volumeSlider.setValue(100);

//		captureButton = new JButton();
		// captureButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/camera.png")));
//		captureButton.setToolTipText("Take picture");

//		ejectButton = new JButton();
		// ejectButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_eject_blue.png")));
//		ejectButton.setToolTipText("Load/eject media");

//		connectButton = new JButton();
		// connectButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/connect.png")));
//		connectButton.setToolTipText("Connect to media");

		fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Play");
		fileChooser.addChoosableFileFilter(SwingFileFilterFactory
				.newVideoFileFilter());
		fileChooser.addChoosableFileFilter(SwingFileFilterFactory
				.newAudioFileFilter());
		fileChooser.addChoosableFileFilter(SwingFileFilterFactory
				.newPlayListFileFilter());
		FileFilter defaultFilter = SwingFileFilterFactory.newMediaFileFilter();
		fileChooser.addChoosableFileFilter(defaultFilter);
		fileChooser.setFileFilter(defaultFilter);

//		subTitlesButton = new JButton("Subs");
		// subTitlesButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/comment.png")));
//		subTitlesButton.setToolTipText("Cycle sub-titles");
	}

	private void layoutControls() {
		setBorder(new EmptyBorder(4, 4, 4, 4));

		setLayout(new BorderLayout());

		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new GridLayout(1, 1));
		// positionPanel.add(positionProgressBar);
		positionPanel.add(positionSlider);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(8, 0));

		topPanel.add(timeLabel, BorderLayout.WEST);
		topPanel.add(positionPanel, BorderLayout.CENTER);
		topPanel.add(chapterLabel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new FlowLayout());

		bottomPanel.add(previousChapterButton);
		bottomPanel.add(rewindButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(pauseButton);
		bottomPanel.add(playButton);
		bottomPanel.add(fastForwardButton);
		bottomPanel.add(nextChapterButton);

		bottomPanel.add(volumeSlider);
		bottomPanel.add(toggleMuteButton);

//		bottomPanel.add(captureButton);

//		bottomPanel.add(ejectButton);
//		bottomPanel.add(connectButton);

//		bottomPanel.add(subTitlesButton);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Broken out position setting, handles updating mediaPlayer
	 */
	private void setSliderBasedPosition() {
		if (!mediaPlayer.isSeekable()) {
			return;
		}
		float positionValue = (float) positionSlider.getValue() / 1000.0f;
		// Avoid end of file freeze-up
		if (positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		mediaPlayer.setPosition(positionValue);
	}

	private void updateUIState() {
		if (!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current
			// position in video
			mediaPlayer.play();
			if (!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}
		long time = mediaPlayer.getTime();
		int position = (int) (mediaPlayer.getPosition() * 1000.0f);
		int chapter = mediaPlayer.getChapter();
		int chapterCount = mediaPlayer.getChapterCount();
		updateTime(time);
		updatePosition(position);
		updateChapter(chapter, chapterCount);
	}

	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if (mediaPlayer.getLength() > 0) {
			mediaPlayer.skip(skipTime);
			updateUIState();
		}
	}

	private void registerListeners() {
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				if (firstVolumeChanged) {
					updateVolume(mediaPlayer.getVolume());
				} else {
					firstVolumeChanged = true;
				}

			}
		});

		positionSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (mediaPlayer.isPlaying()) {
					mousePressedPlaying = true;
					mediaPlayer.pause();
				} else {
					mousePressedPlaying = false;
				}
				setSliderBasedPosition();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setSliderBasedPosition();
				updateUIState();
			}
		});

		previousChapterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.previousChapter();
			}
		});

		rewindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skip(-SKIP_TIME_MS);
			}
		});

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.pause();
			}
		});

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.play();
			}
		});

		fastForwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skip(SKIP_TIME_MS);
			}
		});

		nextChapterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.nextChapter();
			}
		});

		toggleMuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.mute();
			}
		});

		volumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				// if(!source.getValueIsAdjusting()) {
				mediaPlayer.setVolume(source.getValue());
				// }
			}
		});

//		captureButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mediaPlayer.saveSnapshot();
//			}
//		});
//
//		ejectButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mediaPlayer.enableOverlay(false);
//				if (JFileChooser.APPROVE_OPTION == fileChooser
//						.showOpenDialog(PlayerControlsPanel.this)) {
//					mediaPlayer.playMedia(fileChooser.getSelectedFile()
//							.getAbsolutePath());
//				}
//				mediaPlayer.enableOverlay(true);
//			}
//		});
//
//		connectButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mediaPlayer.enableOverlay(false);
//				String mediaUrl = (String) JOptionPane.showInputDialog(
//						PlayerControlsPanel.this, "Enter a media URL",
//						"Connect to media", JOptionPane.QUESTION_MESSAGE);
//				if (mediaUrl != null && mediaUrl.length() > 0) {
//					mediaPlayer.playMedia(mediaUrl);
//				}
//				mediaPlayer.enableOverlay(true);
//			}
//		});
//
//		subTitlesButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int spu = mediaPlayer.getSpu();
//				if (spu > -1) {
//					spu++;
//					if (spu > mediaPlayer.getSpuCount()) {
//						spu = -1;
//					}
//				} else {
//					spu = 0;
//				}
//				mediaPlayer.setSpu(spu);
//			}
//		});
	}

	private final class UpdateRunnable implements Runnable {

		private final MediaPlayer mediaPlayer;

		private UpdateRunnable(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}

		public void run() {
			final long time = mediaPlayer.getTime();
			final int position = (int) (mediaPlayer.getPosition() * 1000.0f);
			final int chapter = mediaPlayer.getChapter();
			final int chapterCount = mediaPlayer.getChapterCount();

			// Updates to user interface components must be executed on the
			// Event
			// Dispatch Thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (mediaPlayer.isPlaying()) {
						updateTime(time);
						updatePosition(position);
						updateChapter(chapter, chapterCount);
					}
				}
			});
		}
	}

	private void updateTime(long millis) {
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		timeLabel.setText(s);
	}

	private void updatePosition(int value) {
		// positionProgressBar.setValue(value);
		positionSlider.setValue(value);
	}

	private void updateChapter(int chapter, int chapterCount) {
		String s = chapterCount != -1 ? (chapter + 1) + "/" + chapterCount
				: "-";
		chapterLabel.setText(s);
		chapterLabel.invalidate();
		validate();
	}

	private void updateVolume(int value) {
		volumeSlider.setValue(value);
	}
}