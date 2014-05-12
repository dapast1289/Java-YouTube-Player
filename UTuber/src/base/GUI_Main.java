package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI_Main {

	public static void main(String[] args) {
		new GUI_Main();
	}

	static JFrame jf;
	static JButton playButton;
	static JButton sButton;
	static JTextField sBar;
	static JPanel sPanel;
	static VLCMediaPlayer player;
	static SidePanel sidepanel;
	static JCheckBox jcb;

	public GUI_Main() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		JFXPanel jp = new JFXPanel();
		jp.setVisible(true);

		jf = new JFrame("Music Player");
		jf.setLayout(new BorderLayout());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		playButton = new JButton("Play");

		sPanel = new JPanel(true);
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.LINE_AXIS));
		sButton = new JButton("GO");
		sButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchAction();
			}
		});

		sBar = new JTextField();
		sBar.requestFocus();

		sBar.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				if ( e.getKeyChar() == KeyEvent.VK_ENTER) {
					searchAction();
				}
			}
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
			}
		});
		
		JLabel preferAudioLabel = new JLabel("Prefer Audio");
		jcb = new JCheckBox();

		
		sPanel.add(preferAudioLabel);
		sPanel.add(jcb);
		sPanel.add(sBar);
		sPanel.add(sButton);

		sidepanel = new SidePanel();
		sidepanel.setPreferredSize(new Dimension(100,100));
		
		sidepanel.setData(Charter.parseCharts());

		player = new VLCMediaPlayer();
		
		SimpleControlPanel controlPanel = new SimpleControlPanel(player.getMediaPlayer());

		jf.add(sPanel, BorderLayout.NORTH);
		jf.add(player, BorderLayout.CENTER);
		jf.add(sidepanel, BorderLayout.WEST);
		jf.add(controlPanel, BorderLayout.SOUTH);
		
		jf.setPreferredSize(new Dimension(1200, 800));

		jf.pack();
		jf.setVisible(true);

	}

	public static void play(String media) {
		System.out.println("playing " + media );
		player.play(media);
	}

	public static String getSearch() {
		return sBar.getText();
	}
	
	public static boolean preferAudio() {
		return jcb.isSelected();
	}

	public static void searchAction() {
		String search = GUI_Main.getSearch();
		if (!search.isEmpty()) {
			ArrayList<String[]> urlArray =  Utubr.Search(search);
			String url = Extractor.extract(urlArray.get(0)[0], urlArray.get(0)[1]).getDecodedStream(0);
			try {
				URL audioURL = new URL(url);
				play(audioURL.toString());
				sidepanel.setData(urlArray);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}
}