package base;

import java.util.ArrayList;

public class VidPage {
	
	ArrayList<VideoStream> array;
	String playerURL;
	public ArrayList<VideoStream> getArray() {
		return array;
	}
	public void setArray(ArrayList<VideoStream> array) {
		this.array = array;
	}
	public String getPlayerURL() {
		return playerURL;
	}
	public void setPlayerURL(String playerURL) {
		this.playerURL = playerURL;
	}
	@Override
	public String toString() {
		return "VidPage [array=" + array + ", playerURL=" + playerURL + "]";
	}
	public VidPage(ArrayList<VideoStream> array, String playerURL) {
		super();
		this.array = array;
		this.playerURL = playerURL;
	}
	
	public String getDecodedStream(int n) {
		String url = array.get(n).getUrl();
		if (url.contains("signature=")){
			return url;
		}
		String decryptedSig = Extractor.decodeSignature(array.get(n).getSig(), playerURL);
		if (decryptedSig == null) {
			return url;
		}
		return url + "&signature=" + decryptedSig;
	}
	
	
}
