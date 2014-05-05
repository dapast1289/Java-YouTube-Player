package base;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SidePanel extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JList<String> jlist = new JList<String>();
	static DefaultListModel<String> listModel = new DefaultListModel<String>();
	static int lastSelectedIndex;
	static ArrayList<String[]> array;

	public SidePanel() {
		setViewportView(jlist);
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
									if (array.get(index)[0] != null) {
										GUI_Main.play(Extractor.extract(array.get(index)[0],array.get(index)[1]).getDecodedStream(0));
										lastSelectedIndex = index;
									} else  if (array.get(index)[1] != null) {
										System.out.println("playing: " + Utubr.Search(array.get(index)[1]).get(0)[0]);
										GUI_Main.play(Extractor.extract(Utubr.Search(array.get(index)[1]).get(0)[0],array.get(index)[1]).getDecodedStream(0));
										lastSelectedIndex = index;
										
									}
									
								}

							}
						});

					}
				});

		jlist.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				setPreferredSize(new Dimension(100, 100));
				revalidate();
				doLayout();

				GUI_Main.jf.revalidate();
				GUI_Main.jf.doLayout();

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setPreferredSize(new Dimension(500, 100));
				revalidate();
				doLayout();

				GUI_Main.jf.revalidate();
				GUI_Main.jf.doLayout();

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
	}

	public void setData(ArrayList<String[]> array) {
		SidePanel.array = array;
		listModel.removeAllElements();
		for (String[] row : array) {
			listModel.addElement(row[1]);
		}
		lastSelectedIndex = -1;
	}

	public static void playNext() {
		if (jlist.getSelectedIndex() < array.size()) {
			jlist.setSelectedIndex(jlist.getSelectedIndex() + 1);
		}
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

}
