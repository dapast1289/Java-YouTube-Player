package base;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class YT_APITest {

	@Test
	public void test() {
		ArrayList<AudioVid> s = YT_API.search("charlie the unicorn", 5);
		for (SearchVid searchVid : s) {
			System.out.println(searchVid.url);
		}
		System.out.println(s.size());
		assertEquals(s.size(), 5);
	}
	
	@Test
	public void testName() throws Exception {
		ArrayList<AudioVid> s = YT_API.getRelated("Fu2DcHzokew", 1);
		for (SearchVid searchVid : s) {
			System.out.println(searchVid.url);
		}
		System.out.println(s.size());
	}
	
	

}
