package gui;

import base.YT_API;

public class SearchPane extends SongList {

	public SearchPane(String search) {
		super();
		setSongs(YT_API.search(search, 50));
	}

}
