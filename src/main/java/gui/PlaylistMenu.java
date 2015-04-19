package gui;

import base.PlaylistDatabase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Jaap Heijligers on 3/26/15.
 */
public class PlaylistMenu extends VBox {

    Set<String> playlistNames;
    PlaylistDatabase db;
    ListView<String> menuList;
    TextField searchField;

    public PlaylistMenu() {
        getStyleClass().add("box");
        menuList = new ListView<>();
        db = PlaylistDatabase.getInstance();
//        setPrefWidth(300);
//        setMinHeight(100);

        setPadding(new Insets(0, 10, 0, 10));

        searchField = new TextField();
        searchField.setPromptText("Add playlist");
        searchField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = searchField.getText();
                if (name == null || name.isEmpty()) return;

                db.addPlaylist(name, new ArrayList<>());
                updatePlaylists();
            }
        });

        Label label = new Label("Playlists");

        MenuItem deletePlaylist = new MenuItem("Detele playlist");
        ContextMenu contextMenu = new ContextMenu(deletePlaylist);
        deletePlaylist.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                db.removePlaylist(menuList.getSelectionModel().getSelectedItem());
                updatePlaylists();
            }
        });

        menuList.setContextMenu(contextMenu);

        updatePlaylists();
        getChildren().addAll(label, searchField, menuList);
    }

    private void updatePlaylists() {
        playlistNames = db.getPlaylists();
        menuList.getItems().clear();
        playlistNames.forEach(s -> menuList.getItems().add(s));

    }

    public ReadOnlyObjectProperty<String> getSelectedItemProperty() {
        return menuList.getSelectionModel().selectedItemProperty();
    }

    public void clearSelection() {
        menuList.getSelectionModel().clearSelection();
    }
}
