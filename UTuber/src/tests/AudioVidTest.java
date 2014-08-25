/**
 * 
 */
package tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import base.AudioVid;
import base.SearchVid;

/**
 * @author Jaap Heijligers
 *
 */
public class AudioVidTest {

	@Test
	public void generateMediaURLTest() {
		AudioVid av = new AudioVid(null, "lel", null, null);
		assertNotNull(av.getMediaURL());
	}
	
	@Test
	public void svToAvTest() throws Exception {
		SearchVid sv = new SearchVid(null, "charlie");
		AudioVid av = new AudioVid(sv, null);
		assertNotNull(av);
	}
	
}
