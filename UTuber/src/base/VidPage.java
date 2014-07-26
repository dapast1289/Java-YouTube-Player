package base;

import java.util.ArrayList;
import java.util.Arrays;

public class VidPage {

    ArrayList<MediaStream> array;
    String playerURL;

    public ArrayList<MediaStream> getArray() {
	return array;
    }

    public void setArray(ArrayList<MediaStream> array) {
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
	return "VidPage [array=" + Arrays.deepToString(array.toArray())
		+ ", playerURL=" + playerURL + "]";
    }

    public VidPage(ArrayList<MediaStream> array, String playerURL) {
	super();
	this.array = array;
	this.playerURL = playerURL;
    }

    public String getDecodedStream(int n) {
	String url = array.get(n).getUrl();
	if (url.contains("signature=")) {
	    return url;
	}
	String decryptedSig = Extractor.decryptSignature(array.get(n).getSig(),
		playerURL);
	if (decryptedSig == null) {
	    return url;
	}
	return url + "&signature=" + decryptedSig;
    }

    public String getDecodedStream(MediaStream ms) {
	String url = ms.getUrl();
	if (url.contains("signature=")) {
	    return url;
	}
	String decryptedSig = Extractor
		.decryptSignature(ms.getSig(), playerURL);
	if (decryptedSig == null) {
	    return url;
	}
	return url + "&signature=" + decryptedSig;
    }

    public String getAudioStream() {

	for (MediaStream ms : array) {
	    if (ms.getType().equals("audio;webm")) {
		return getDecodedStream(ms);
	    }
	}
	for (MediaStream ms : array) {
	    if (ms.getType().equals("audio;mp4")) {
		return getDecodedStream(ms);
	    }
	}
	System.err.println("Could not find suitable audio stream");
	return getDecodedStream(0);
    }

    public String getMp4Audio() {
	for (MediaStream ms : array) {
	    if (ms.getType().equals("audio;mp4")) {
		return getDecodedStream(ms);
	    }
	}
	System.err.println("Could not find suitable mp3 stream");
	return getDecodedStream(0);
    }

    public String getWebMAudio() {
	for (MediaStream ms : array) {
	    if (ms.getType().equals("audio;webm")) {
		return getDecodedStream(ms);
	    }
	}
	System.err.println("Could not find suitable webm stream");
	return getDecodedStream(0);
    }

    public MediaStream getAudioMS() {
	for (MediaStream ms : array) {
	    if (ms.getType().startsWith("audio")) {
		return ms;
	    }
	}
	System.err.println("Could not find suitable audio stream");
	return array.get(0);
    }

    public void add(MediaStream ms) {
	array.add(ms);
    }

    public String getSmall() {
	for (MediaStream ms : array) {
	    if (ms.getQuality().startsWith("small")) {
		return getDecodedStream(ms);
	    }
	}
	for (MediaStream ms : array) {
	    if (ms.getQuality().startsWith("medium")) {
		return getDecodedStream(ms);
	    }
	}
	return getDecodedStream(array.get(0));
    }

    public MediaStream getSmallMS() {
	for (MediaStream ms : array) {
	    if (ms.getQuality().startsWith("small")) {
		return (ms);
	    }
	}
	for (MediaStream ms : array) {
	    if (ms.getQuality().startsWith("medium")) {
		return (ms);
	    }
	}
	return (array.get(0));
    }

}
