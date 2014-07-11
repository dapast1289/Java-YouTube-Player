package base;

public class AudioVid extends SearchVid {
    String mediaURL;

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

    public AudioVid(String id, String title, String mediaURL) {
	super(id, title);
	this.mediaURL = mediaURL;
    }

    public AudioVid(SearchVid sv, String mediaURL) {
	super(sv.id, sv.title);
	this.mediaURL = mediaURL;
    }

    public void generateMediaURL() {
	if (mediaURL != null) {
	    return;
	}
	try {

	    mediaURL = Extractor.extractFmt(this).getAudioStream();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(title + "\n" + url);
	}
    }

    public boolean hasMediaURL() {
	return mediaURL != null;
    }

}
