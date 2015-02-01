/**
*
*/
package base;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
* @author jaap
*
*/
public class ExtractorTest {
	AudioVid av;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		new JFXPanel();
		av = YT_API.search("charlie", 1).get(0);

	}

	/**
	 * Test method for {@link base.Extractor#searchVidPage(java.lang.String)}.
	 */
	@Test
	public void testSearchVidPage() {
		VidPage vp = Extractor.searchVidPage("charlie");
		assertTrue(vp.getArray().size() > 2);
	}

	/**
	 * Test method for {@link base.Extractor#extractFmt(base.SearchVid)}.
	 */
	@Test
	public void testExtractFmt() {
		VidPage list = Extractor.extractFmt(av);
		assertTrue(list.getArray().size() > 2);
	}

	/**
	 * Test method for
	 * {@link base.Extractor#decryptSignature(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testDecryptSignature() {
		AudioVid katy = YT_API.search("katy perry", 1).get(0);
		VidPage list = Extractor.extractFmt(katy);
		MediaStream ms = list.getAudioMS();
		String sig = Extractor.decryptSignature(ms.getSig(),
				list.getPlayerURL());
		assertTrue(sig.length() > 70);
	}

	/**
	 * Test method for {@link base.Extractor#getStreamMap(java.lang.String)}.
	 */
	@Test
	public void testGetStreamMap() {
		try {
			URL url = new URL("https://www.youtube.com/watch?v=CsGYh8AacgY");
			String data = Extractor.urlToString(url);
			String sm = Extractor.getStreamMap(data);
			assertTrue(sm.length() > 100);
		} catch (MalformedURLException e) {
			fail();
			e.printStackTrace();
		}

	}

	/**
	 * Test method for {@link base.Extractor#httpToString(java.net.URL)}.
	 */
	@Test
	public void testHttpToString() {
		VidPage list = Extractor.extractFmt(av);
		try {
			String player = Extractor.httpToString(new URL(list.getPlayerURL()));
			assertTrue(player.length() > 100);
		} catch (MalformedURLException e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link base.Extractor#urlToString(java.net.URL)}.
	 */
	@Test
	public void testUrlToString() {
		URL url;
		try {
			url = new URL("https://www.youtube.com/watch?v=CsGYh8AacgY");
			String data = Extractor.urlToString(url);
			assertTrue(data.length() > 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		}
	}

}
