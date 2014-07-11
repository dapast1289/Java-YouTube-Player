package base;

class SearchVid {
    String url;
    String title;
    String id;

    public SearchVid(String id, String title) {
	this.id = id;
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

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }
}