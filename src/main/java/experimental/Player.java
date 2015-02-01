package experimental;

import java.io.IOException;

import base.YT_API;

public class Player  {
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: play <query>");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string + " ");
		}
		
		System.out.println("Searching for " + sb.toString());
		
		String url = YT_API.getURL(sb.toString());
		
		System.out.println("Playing: " + url);
		
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
