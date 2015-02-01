package base;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
	public static void writeTo(String content, String filename) {
		try {
			FileWriter f = new FileWriter(filename);
			f.write(content);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
