/**
 *
 */
package base;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Jaap Heijligers
 */
public class AudioVidTest {

    @Before
    public void setUp() {
        new JFXPanel();
    }

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
