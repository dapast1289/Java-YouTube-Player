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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

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
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				"C:/Program Files/VideoLAN/VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    		try {
    			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		} catch (UnsupportedLookAndFeelException e) {
    			e.printStackTrace();
    		} catch (ClassNotFoundException e1) {
    			e1.printStackTrace();
    		} catch (InstantiationException e1) {
    			e1.printStackTrace();
    		} catch (IllegalAccessException e1) {
    			e1.printStackTrace();
    		}

		jf = new JFrame("Music Player");
		jf.setLayout(new BorderLayout());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		playButton = new JButton("Play");

		sPanel = new JPanel(true);
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.LINE_AXIS));
		sButton = new JButton("GO");
		sButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				searchAction();
			}
		});

		sBar = new JTextField();
		sBar.requestFocus();

		sBar.addKeyListener(new KeyListener() {

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
		sidepanel.setPreferredSize(new Dimension(300,300));
		
		sidepanel.setData(Charter.getMostShared());

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
			ArrayList<AudioVid> urlArray = YT_API.search(search, 50);
			String url = Extractor.extract(urlArray.get(0)).getDecodedStream(0);
			try {
				URL audioURL = new URL(url);
				play(audioURL.toString());
				sidepanel.setData(urlArray);
				SidePanel.playNext();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}
}