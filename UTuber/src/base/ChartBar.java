package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class ChartBar extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5190445627066306414L;

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.add(new ChartBar(null));
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	private JRadioButton mSharedButton;
	private JRadioButton mStreamedButton;

	private JComboBox<String> countryBox;
	private JComboBox<String> dateBox;

	private SidePanel parent;

	public ChartBar(SidePanel parent) {
		super();
		this.parent = parent;

		mSharedButton = new JRadioButton("Most Shared");
		mStreamedButton = new JRadioButton("Most Streamed");

		ButtonGroup bg = new ButtonGroup();
		bg.add(mSharedButton);
		bg.add(mStreamedButton);

		mSharedButton.setSelected(true);
		mStreamedButton.setSelected(false);

		String[] countries = jsonUrlToStringArray("http://charts.spotify.com/api/charts/most_shared/");
		countryBox = new JComboBox<String>(countries);
		String[] dates = jsonUrlToStringArray("http://charts.spotify.com/api/charts/most_shared/global");
		dateBox = new JComboBox<String>(dates);

		add(mSharedButton);
		add(mStreamedButton);
		add(countryBox);
		add(dateBox);

		mSharedButton.addActionListener(this);
		mStreamedButton.addActionListener(this);
		countryBox.addActionListener(this);
		dateBox.addActionListener(this);

		countryBox.setSelectedIndex(countryBox.getModel().getSize() - 1);
	}

	public TrackList getTrackList() {
		String url = "http://charts.spotify.com/api/charts/";
		if (mSharedButton.isSelected()) {
			url += "most_shared/";
		} else {
			url += "most_streamed/";
		}

		url += countryBox.getSelectedItem() + "/";

		url += dateBox.getSelectedItem();

		System.out.println("url: " + url);

		try {
			String trackListJSON = Extractor.urlToString(new URL(url));

			Gson gson = new Gson();

			TrackList tl = gson.fromJson(trackListJSON, TrackList.class);
			return tl;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] jsonUrlToStringArray(String url) {
		String[] countries = null;
		try {
			String countryList = Extractor.urlToString(new URL(url));
			Gson gson = new Gson();
			countries = gson.fromJson(countryList, String[].class);
			System.out.println(countries);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return countries;
	}

	public void actionPerformed(ActionEvent e) {
		TrackList tl = getTrackList();
		List<Track> atl = tl.getTracks();

		ArrayList<AudioVid> tracks = new ArrayList<AudioVid>();
		for (Track track : atl) {
			tracks.add(new AudioVid(null, track.getTrack_name() + " - "
					+ track.getArtist_name(), null));
		}

		parent.setData(tracks);

	}
}

@Generated("org.jsonschema2pojo")
class Track {

	@Expose
	private String date;
	@Expose
	private String country;
	@Expose
	private String track_url;
	@Expose
	private String track_name;
	@Expose
	private String artist_name;
	@Expose
	private String artist_url;
	@Expose
	private String album_name;
	@Expose
	private String album_url;
	@Expose
	private String artwork_url;
	@Expose
	private Integer num_streams;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTrack_url() {
		return track_url;
	}

	public void setTrack_url(String track_url) {
		this.track_url = track_url;
	}

	public String getTrack_name() {
		return track_name;
	}

	public void setTrack_name(String track_name) {
		this.track_name = track_name;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public String getArtist_url() {
		return artist_url;
	}

	public void setArtist_url(String artist_url) {
		this.artist_url = artist_url;
	}

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public String getAlbum_url() {
		return album_url;
	}

	public void setAlbum_url(String album_url) {
		this.album_url = album_url;
	}

	public String getArtwork_url() {
		return artwork_url;
	}

	public void setArtwork_url(String artwork_url) {
		this.artwork_url = artwork_url;
	}

	public Integer getNum_streams() {
		return num_streams;
	}

	public void setNum_streams(Integer num_streams) {
		this.num_streams = num_streams;
	}

}

class TrackList {

	@Expose
	private List<Track> tracks = new ArrayList<Track>();

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

}