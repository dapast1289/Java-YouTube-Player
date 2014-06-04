package base;

class SearchVid {
	String url;
	String title;
	
	public SearchVid(String id, String title) {
		if (id == null) {
			this.url = null;
		} else {
			this.url = "http://www.youtube.com/watch?v=" + id;
		}
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String id) {
		this.url = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}