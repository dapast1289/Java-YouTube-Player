package base;

public class MediaStream {
	private String url;
	private String quality;
	private int itag;
	String sig;
	private String type;
	
	
	public MediaStream(String url, String quality, int itag, String sig, String type) {
		super();
		this.url = url;
		this.quality = quality;
		this.itag = itag;
		this.sig = sig;
		this.type = type;
		System.out.println("type: " + type);
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
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
