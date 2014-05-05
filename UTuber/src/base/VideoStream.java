package base;

public class VideoStream {
	private String url;
	private String quality;
	private int itag;
	String sig;
	
	
	public VideoStream(String url, String quality, int itag, String sig) {
		super();
		this.url = url;
		this.quality = quality;
		this.itag = itag;
		this.sig = sig;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public int getItag() {
		return itag;
	}
	public void setItag(int itag) {
		this.itag = itag;
	}
	
	
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	@Override
	public String toString() {
		return "VideoStream [url=" + url + ", quality=" + quality + ", itag="
				+ itag + "]";
	}
	
}
