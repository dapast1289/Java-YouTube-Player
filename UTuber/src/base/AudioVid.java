package base;

public class AudioVid extends SearchVid {
	String mediaURL;
	String iconURL;

	public String getMediaURL() {
		if (!hasMediaURL()) {
			System.out.println("Generating MediaURL: " + title);
			generateMediaURL();
		}
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public AudioVid(String id, String title, String mediaURL, String iconURL) {
		super(id, title);
		this.mediaURL = mediaURL;
		this.iconURL = iconURL;
	}

	public AudioVid(SearchVid sv, String mediaURL) {
		super(sv.id, sv.title);
		this.mediaURL = mediaURL;
	}

	public void generateMediaURL() {
		if (mediaURL != null) {
			return;
		}

		generateURL();
		
		try {
			mediaURL = Extractor.extractFmt(this).getAudioStream();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(title + "\n" + url);
		}
	}
	
	private void generateURL() {
		if (url != null) {
			return;
		}
		
		AudioVid tmp = YT_API.search(title, 1).get(0);
		url = tmp.url;
		title = tmp.title;
	}

	public boolean hasMediaURL() {
		return mediaURL != null;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	

}
