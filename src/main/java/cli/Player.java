package cli;

import base.AudioVid;
import base.VideoExtractor;
import base.YT_API;

import java.io.IOException;
import java.util.ArrayList;

public class Player  {
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage: play <query>");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string).append(" ");
		}
		
		System.out.println("Searching for " + sb.toString());
		
		ArrayList<AudioVid> vids = YT_API.search(sb.toString(), 1);
		if (vids == null || vids.size() == 0) {
			System.out.printf("No videos found for " + sb.toString());
			return;
		}

		String url = VideoExtractor.getInstance().extractVideo(vids.get(0).getUrl());
		if (url == null) {
			System.out.println("Extraction failed for: " + vids.get(0));
			return;
		}
		play(url);
	}
	
	private static void play(String s) {
		try {
			System.out.println("Playing: " + s);
			Runtime.getRuntime().exec("vlc " + s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
