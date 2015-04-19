package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MenuList extends VBox {

    public final String TOP_LISTS = "Top Lists";
    public final String RADIO = "Radio";
    PlaylistMenu playlistMenu;
    PlaylistPane playlistPane;
    private ChartsPane toplists;
    private RadioPane radioPane;
    private ListView<String> itemList;
    private Main main = Main.getInstance();
    private SongDisplay songDisplay = SongDisplay.getInstance();
    private final static int ROW_HEIGHT = 36;

    public MenuList() {
        super();
        ArrayList<String> menuElements = new ArrayList<String>();
        menuElements.add(TOP_LISTS);
        menuElements.add(RADIO);

        getStyleClass().add("menulist");

        ObservableList<String> observableList = FXCollections.observableList(menuElements);
        itemList = new ListView<>(observableList);
        itemList.getSelectionModel().selectFirst();
        itemList.setPrefHeight(itemList.getItems().size() * ROW_HEIGHT + 2);

        toplists = new ChartsPane();
        radioPane = new RadioPane();
        playlistPane = new PlaylistPane();
        playlistMenu = new PlaylistMenu();

        itemList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {

                    public void changed(ObservableValue<? extends String> arg0,
                                        String oldValue, String newValue) {
                        System.out.println("Changed from " + oldValue + " to "
                                + newValue);
                        if (newValue == null) return;
                        if (newValue.equals(TOP_LISTS)) {
                            main.setCenter(toplists);
                        } else if (newValue.equals(RADIO)) {
                            main.setCenter(radioPane);
                        } else {
                            main.setCenter(new HBox());
                        }
                        playlistMenu.clearSelection();
                    }
                });

        playlistMenu.getSelectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            playlistPane.setPlaylist(newValue);
            main.setCenter(playlistPane);
            itemList.getSelectionModel().clearSelection();
        });

        getChildren().addAll(itemList, playlistMenu, songDisplay);
        songDisplay.setAlignment(Pos.BOTTOM_CENTER);


        Platform.runLater(() -> main.setCenter(toplists));
        setMaxHeight(Double.MAX_VALUE);
        setPrefHeight(Double.MAX_VALUE);
    }

    public ListView<String> getItemList() {
        return itemList;
    }

    public void setItemList(ListView<String> itemList) {
        this.itemList = itemList;
    }
}
