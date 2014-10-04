package gui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import base.AudioVid;
import base.SearchVid;
import base.YT_API;

public class RadioPane extends VBox {

	TextField tf;
	Label inputlabel;
	SongList songList;
	AudioPlayer audioPlayer = AudioPlayer.getInstance();

	public RadioPane() {
		super();
		
		getStyleClass().add("radiopane");
		
		tf = new TextField();
		tf.getStyleClass().add("textfield");
		addOnEnter(tf);
		
		inputlabel = new Label("Enter artist or song name");
		inputlabel.getStyleClass().add("content-text");
		
		songList = new SongList();
		songList.setPrefHeight(1000);
		
		showSearch();
		
		
	}
	
	public void addOnEnter(final TextField tf) {
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER && !tf.getText().isEmpty()) {
					System.out.println("Starting Radio: " + tf.getText());

					AudioVid origin = YT_API.search(tf.getText(), 1).get(0);
					ArrayList<AudioVid> extraList = YT_API.getRelated(origin.id, 50);
					
					Collections.shuffle(extraList, new Random());
					ArrayList<AudioVid> list = new ArrayList<AudioVid>();
					list.add(origin);
					list.addAll(extraList);
					
					songList.setSongs(list);
					audioPlayer.playSongs(songList, 0);
					
				}
			}
		});
	}
	
	public void showSearch() {
		getChildren().clear();
		getChildren().add(inputlabel);
		getChildren().add(tf);
		getChildren().add(songList);
	}

}
