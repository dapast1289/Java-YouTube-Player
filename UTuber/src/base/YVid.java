package base;

import java.util.ArrayList;


public class YVid {
	
	ArrayList<String[]> array;
	String title;

	public YVid(ArrayList<String[]> array, String title) {
		this.array = array;
		this.title = title;
		GUI_Main.sidepanel.setData(array);
	}
	
	public String getAudio() {
		String audioStream = null;
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i)[0].contains("audio/mp4")) {
				audioStream = array.get(i)[1];
				break;
			}
		}
		System.out.println("audio url = " + audioStream);
		return audioStream;
	}
	
	public String getHDStream() {
		String HDStream = null;
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i)[0].contains("video/mp4")) {
				HDStream = array.get(i)[1];
				break;
			}
		}
		System.out.println("HD url = " + HDStream);
		return HDStream;
	}
	
	public String getWebM() {
		String HDStream = null;
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i)[0].contains("video/webm")) {
				HDStream = array.get(i)[1];
				break;
			}
		}
		System.out.println("webm url = " + HDStream);
		return HDStream;
	}
	
	public String getFirst() {
		if (array.size() > 0)
			return array.get(0)[1];
		return null;
	}
	

	public ArrayList<String[]> getArray() {
		return array;
	}

	public void setArray(ArrayList<String[]> array) {
		this.array = array;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
