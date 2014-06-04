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
	static ArrayList<SearchVid> array;
	static JScrollPane jsp = new JScrollPane();

	public SidePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		ChartBar cb = new ChartBar(this);
		add(cb);
		add(jsp);

		jsp.setViewportView(jlist);
		jlist.setModel(listModel);

		array = new ArrayList<>();
		lastSelectedIndex = 0;

		jlist.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						jlist.repaint();
						SwingUtilities.invokeLater(new Runnable() {

							@Override
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

	public void setData(ArrayList<SearchVid> array) {
		SidePanel.array = array;
		listModel.removeAllElements();
		for (SearchVid row : array) {
			listModel.addElement(row.title);
		}
		lastSelectedIndex = -1;
	}

	public static String getSelectedAudio() {
		int index = jlist.getSelectedIndex();
		if (array.get(index).url != null) {
			return Extractor.extractFmt(array.get(index).url,
					array.get(index).title).getAudioStream();
		}
		String title = array.get(index).title;
		return Extractor.extractFmt(YT_API.search(title, 1).get(0).url, title)
				.getAudioStream();

	}

	public static String getSelectedVideo() {
		int index = jlist.getSelectedIndex();
		if (array.get(index).url != null) {
			return Extractor.extract(array.get(index).url, array.get(index).title)
					.getDecodedStream(0);
		}
		String title = array.get(index).title;
		return Extractor.extract(YT_API.search(title, 1).get(0).url,
				title).getDecodedStream(0);
	}

	public static void playNext() {
		if (jlist.getSelectedIndex() < array.size()) {
			jlist.setSelectedIndex(jlist.getSelectedIndex() + 1);
			lastSelectedIndex = jlist.getSelectedIndex();
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

	public static ArrayList<SearchVid> getArray() {
		return array;
	}

	public static void setArray(ArrayList<SearchVid> array) {
		SidePanel.array = array;
	}

}
