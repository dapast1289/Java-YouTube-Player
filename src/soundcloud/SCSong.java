package soundcloud;

public class SCSong {

	String id, title, permalink, username, artworkURL;
	int duration;
	boolean downloadable;

	public SCSong(String id, String title, String permalink, String username, String artworkURL, int duration,
			boolean downloadable) {
		super();
		this.id = id;
		this.title = title;
		this.permalink = permalink;
		this.username = username;
		this.artworkURL = artworkURL;
		this.duration = duration;
		this.downloadable = downloadable;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the permalink
	 */
	public String getPermalink() {
		return permalink;
	}

	/**
	 * @param permalink
	 *            the permalink to set
	 */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the artworkURL
	 */
	public String getArtworkURL() {
		return artworkURL;
	}

	/**
	 * @param artworkURL
	 *            the artworkURL to set
	 */
	public void setArtworkURL(String artworkURL) {
		this.artworkURL = artworkURL;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the downloadable
	 */
	public boolean isDownloadable() {
		return downloadable;
	}

	/**
	 * @param downloadable
	 *            the downloadable to set
	 */
	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SCSong [id=" + id + ", title=" + title + ", permalink=" + permalink + ", username=" + username
				+ ", artworkURL=" + artworkURL + ", duration=" + duration + ", downloadable=" + downloadable + "]";
	}

}
