package gui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import base.AudioVid;
import base.ItunesCharts;
import base.SpotiCharts;

public class TopLists extends HBox {
	SongList left;
	SongList right;

	public TopLists() {
		super();
		left = new SongList();
		left.setPrefWidth(10000);

		right = new SongList();
		right.setPrefWidth(10000);

		setMaxWidth(10000);

		getChildren().add(left);
		getChildren().add(right);

		Platform.runLater(new Runnable() {

			public void run() {
				setSongs();
			}
		});
	}

	public void setSongs() {
		Task<ArrayList<AudioVid>> leftTask = new Task<ArrayList<AudioVid>>() {
			@Override
			protected ArrayList<AudioVid> call() throws Exception {
				return ItunesCharts.getTopSongs();
			}
		};

		Task<ArrayList<AudioVid>> rightTask = new Task<ArrayList<AudioVid>>() {
			@Override
			protected ArrayList<AudioVid> call() throws Exception {
				return SpotiCharts.getMostStreamed();
			}
		};

		Thread leftTaskThread = new Thread(leftTask);
		Thread rightTaskThread = new Thread(rightTask);
		leftTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				Platform.runLater(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						left.setSongs((ArrayList<AudioVid>) event.getSource()
								.getValue());
					}
				});
				rightTaskThread.start();
			}
		});

		rightTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				Platform.runLater(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						right.setSongs((ArrayList<AudioVid>) event.getSource()
								.getValue());
					}
				});
			}
		});
		leftTaskThread.start();
	}

}
