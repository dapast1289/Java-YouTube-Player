package base;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SidePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JList<String> jlist = new JList<String>();
	static DefaultListModel<String> listModel = new DefaultListModel<String>();
	static int lastSelectedIndex;
	static ArrayList<AudioVid> array;
	static JScrollPane jsp = new JScrollPane();

	public SidePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		ChartBar cb = new ChartBar(this);
		add(cb);
		add(jsp);

		jsp.setViewportView(jlist);
		jlist.setModel(listModel);

		array = new ArrayList<AudioVid>();
		lastSelectedIndex = 0;

		jlist.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						jlist.repaint();
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								int index = jlist.getSelectedIndex();
								if (index != -1 && index != lastSelectedIndex) {
									if (GUI_Main.preferAudio()) {
										GUI_Main.play(getSelectedAudio());
									} else {
										GUI_Main.play(getSelectedVideo());
									}
									lastSelectedIndex = index;
								}

							}
						});

					}
				});
	}

	public void setData(ArrayList<AudioVid> arrayList) {
		SidePanel.array = arrayList;
		listModel.removeAllElements();
		for (SearchVid row : arrayList) {
			listModel.addElement(row.title);
		}
	}

	public static String getSelectedAudio() {
		int index = jlist.getSelectedIndex();
		SearchVid vid = array.get(index);
		if (vid.url != null) {
			return Extractor.extractFmt(vid).getAudioStream();
		}
		vid = YT_API.search(vid.title, 1).get(0);
		return Extractor.extractFmt(vid).getAudioStream();
	}

	public static String getSelectedAudioMp4Preffered() {
		int index = jlist.getSelectedIndex();
		SearchVid vid = array.get(index);
		if (array.get(index).url != null) {
			return Extractor.extractFmt(vid).getMp4Audio();
		}
		vid = YT_API.search(vid.title, 1).get(0);
		return Extractor.extractFmt(vid).getMp4Audio();
	}

	public static String getSelectedVideo() {
		int index = jlist.getSelectedIndex();
		SearchVid vid = array.get(index);
		if (array.get(index).url != null) {
			return Extractor.extract(vid).getDecodedStream(0);
		}
		vid = YT_API.search(vid.title, 1).get(0);
		return Extractor.extract(vid)
				.getDecodedStream(0);
	}

	public static void playNext() {
		if (jlist.getSelectedIndex() < array.size()) {
			jlist.setSelectedIndex(jlist.getSelectedIndex() + 1);
		}
	}

	public static String getSelectedName() {
		return jlist.getSelectedValue();
	}

	public static JList<String> getJlist() {
		return jlist;
	}

	public static void setJlist(JList<String> jlist) {
		SidePanel.jlist = jlist;
	}

	public static DefaultListModel<String> getListModel() {
		return listModel;
	}

	public static void setListModel(DefaultListModel<String> listModel) {
		SidePanel.listModel = listModel;
	}

	public static int getLastSelectedIndex() {
		return lastSelectedIndex;
	}

	public static void setLastSelectedIndex(int lastSelectedIndex) {
		SidePanel.lastSelectedIndex = lastSelectedIndex;
	}

	public static ArrayList<AudioVid> getArray() {
		return array;
	}

	public static void setArray(ArrayList<AudioVid> array) {
		SidePanel.array = array;
	}

}
