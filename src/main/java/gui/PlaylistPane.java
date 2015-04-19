package gui;

import base.PlaylistDatabase;
import base.Song;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Created by Jaap Heijligers on 3/26/15.
 */
public class PlaylistPane extends SongList {

    private String playlist;
    private PlaylistDatabase db = PlaylistDatabase.getInstance();
    private List<Song> songs;
    private TextField searchField;

    public void setPlaylist(String playlist) {
        this.playlist = playlist;

        songs = db.getPlaylist(playlist);
        setSongs(songs);

        MenuItem deleteItem = new MenuItem("Detele");
        ContextMenu contextMenu = new ContextMenu(deleteItem);
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                db.removeFromPlaylist(playlist, getSelectionModel().getSelectedItem());
                getItems().remove(getSelectionModel().getSelectedIndex());
            }
        });

        setContextMenu(contextMenu);

    }
}
