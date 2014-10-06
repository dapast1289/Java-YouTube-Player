package soundcloud;

public class ExtractedSCSong extends SCSong {

	String downloadURL;

	public ExtractedSCSong(String id, String title, String permalink, String username, String artworkURL, int duration,
			boolean downloadable, String downloadURL) {
		super(id, title, permalink, username, artworkURL, duration, downloadable);
		this.downloadURL = downloadURL;
	}

	public ExtractedSCSong(SCSong song, String downloadURL) {
		this(song.id, song.title, song.permalink, song.username, song.artworkURL, song.duration, song.downloadable,
				downloadURL);
	}

	/**
	 * @return the downloadURL
	 */
	public String getDownloadURL() {
		return downloadURL;
	}

	/**
	 * @param downloadURL
	 *            the downloadURL to set
	 */
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExtractedSCSong [downloadURL=" + downloadURL + ", id=" + id + ", title=" + title + ", username="
				+ username + ", artworkURL=" + artworkURL + ", duration=" + duration + ", downloadable=" + downloadable
				+ "]";
	}

}
