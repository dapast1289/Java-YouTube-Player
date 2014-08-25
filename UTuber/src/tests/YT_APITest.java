package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import base.AudioVid;
import base.YT_API;

public class YT_APITest {

	@Test
	public void searchTest() {
		ArrayList<AudioVid> searchList = YT_API.search("charlie", 20);
		assertEquals(searchList.size(), 20);
	}
	
	@Test
	public void nottakennametest() throws Exception {
		ArrayList<AudioVid> searchList = YT_API.getRelated("CsGYh8AacgY", 20);
		assertEquals(searchList.size(), 20);
	}

}
