package gui;

import base.Song;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends HBox {

    private AudioMediaPlayerComponent vlc;
    private MediaPlayer player;
    private ArrayList<? extends Song> songArray;
    private SongList songList;
    private int current;
    private Song currentSong;
    private Main main = Main.getInstance();
    private Button nextButton;
    private Button playPauseButton;
    // private Button openInVlcButton;
    private Slider slider;
    private boolean isDragging = false;
    private final ScheduledExecutorService sliderUpdater = Executors.newSingleThreadScheduledExecutor();
    private SongDisplay songDisplay = SongDisplay.getInstance();
    static private AudioPlayer audioPlayer;

    public static AudioPlayer getInstance() {
        return audioPlayer;
    }

    public AudioPlayer() {
        super();
        audioPlayer = this;
        getStyleClass().addAll("box", "bar");

        setPrefHeight(50);
        setAlignment(Pos.CENTER);

        initVLC();

        vlc = new AudioMediaPlayerComponent();
        player = vlc.getMediaPlayer();
        addMPListener(player);

        nextButton = new Button("▶▶");
        nextButton.setPrefWidth(50);
        nextButton.setOnAction(arg0 -> playNext());

        playPauseButton = new Button("▐▐ ");
        playPauseButton.setPrefWidth(50);
        playPauseButton.setOnAction(event -> {
            Button source = (Button) event.getSource();
            if (player.isPlaying()) {
                player.pause();
                source.setText("▶");
            } else {
                if (player.isPlayable()) {
                    player.play();
                    source.setText("▐▐ ");
                }
            }
        });
        //
        // openInVlcButton = new Button("Open in vlc");
        // openInVlcButton.setOnAction(new EventHandler<ActionEvent>() {
        //
        // @Override
        // public void handle(ActionEvent arg0) {
        // String stream = Extractor.extract(currentSong).getDecodedStream(0);
        // try {
        // player.pause();
        // System.out.println("Opening stream: " + stream);
        // Runtime.getRuntime().exec("vlc " + stream);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // });

        slider = new Slider(0f, 1f, 0f);
        sliderUpdater.scheduleAtFixedRate(new Updater(player), 0L, 50L, TimeUnit.MILLISECONDS);

        slider.setOnMousePressed(event -> isDragging = true);

        slider.setOnMouseReleased(event -> {
            long newValue = (long) (slider.getValue() * player.getLength());
            player.setTime(newValue);
            isDragging = false;

            System.out.println(player.getMediaDetails().getAudioDescriptions());
            System.out.println(player.getMediaDetails().getSpuDescriptions());
            System.out.println(player.getMediaDetails());

        });
        setHgrow(slider, Priority.ALWAYS);

        getChildren().add(playPauseButton);
        getChildren().add(nextButton);
        // getChildren().add(openInVlcButton);
        getChildren().add(slider);
    }

    private void initVLC() {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "classes/VLC");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "VLC");
        try {
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void playSongs(SongList sl, int index) {
        if (songList != null) {
            songList.clearCurrent();
        }
        songList = sl;
        songArray = sl.getSongList();
        playSong(songArray.get(index));
        current = index;
        sl.setCurrent(current);
    }

    private void playSong(Song av) {
        currentSong = av;
        if (!av.hasMediaURL()) {
            startMediaThread();
        } else {
            player.playMedia(av.getMediaURL());
        }

        songDisplay.setSong(currentSong);
        main.setTitle(currentSong.getTitle());

        System.out.println("Now playing: " + av.getTitle());

    }

    protected void startMediaThread() {
        Task<String> mediaTask = new Task<String>() {

            @Override
            protected String call() throws Exception {
                String mediaURL = currentSong.getMediaURL();
                return mediaURL;
            }
        };

        mediaTask.setOnSucceeded(event -> new Thread(() -> {
            player.playMedia(currentSong.getMediaURL());
        }).start());

        Thread mediaThread = new Thread(mediaTask);
        mediaThread.start();

    }

    public void playNext() {
        songList.next();
        current++;
        if (current >= songArray.size()) {
            current = 0;
        }
        playSong(songArray.get(current));
    }

    public void stop() {
        player.stop();
    }

    private void addMPListener(MediaPlayer mp) {
        mp.addMediaPlayerEventListener(new MediaPlayerEventListener() {

            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                System.out.println("AudioPlayer.videoOutput");
            }

            public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
                System.out.println("AudioPlayer.titleChanged");
            }

            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }

            public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
                System.out.println("AudioPlayer.subItemPlayed");
            }

            public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
                System.out.println("AudioPlayer.subItemFinished");
            }

            public void stopped(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.stopped");

            }

            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("AudioPlayer.snapshotTaken");
            }

            public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
                System.out.println("AudioPlayer.seekableChanged to " + newSeekable);
            }

            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
            }

            public void playing(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.playing");
            }

            public void paused(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.paused");
            }

            public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
                System.out.println("AudioPlayer.pausableChanged to " + newPausable);
            }

            public void opening(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.opening");
            }

            public void newMedia(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.newMedia");
            }

            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                System.out.println("AudioPlayer.mediaSubItemAdded");
            }

            public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
                System.out.println("AudioPlayer.mediaStateChanged to " + newState);
                System.out.println(mediaPlayer.getMediaState());
            }

            public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
                System.out.println("AudioPlayer.mediaParsedChanged");
            }

            public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
                System.out.println("AudioPlayer.mediaMetaChanged");
            }

            public void mediaFreed(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.mediaFreed");
            }

            public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
                System.out.println("AudioPlayer.mediaDurationChanged");
            }

            public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
                System.out.println("AudioPlayer.mediaChanged");
            }

            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                System.out.println("AudioPlayer.lengthChanged");
            }

            public void forward(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.forward");
            }

            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.finished");
                Platform.runLater(() -> playNext());
            }

            public void error(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.error");
            }

            public void endOfSubItems(MediaPlayer mediaPlayer) {
                playNext();
                System.out.println("AudioPlayer.endOfSubItems");
            }

            public void buffering(MediaPlayer mediaPlayer, float newCache) {
            }

            public void backward(MediaPlayer mediaPlayer) {
                System.out.println("AudioPlayer.backward");
            }

            @Override
            public void scrambledChanged(MediaPlayer mediaPlayer,
                                         int newScrambled) {
                System.out.println("AudioPlayer.scrambledChanged");
            }

            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer,
                                              int type, int id) {
                System.out.println("AudioPlayer.elementaryStreamAdded");
            }

            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer,
                                                int type, int id) {
                System.out.println("AudioPlayer.elementaryStreamDeleted");
            }

            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer,
                                                 int type, int id) {
                System.out.println("AudioPlayer.elementaryStreamSelected");
            }
        });
    }

    final class Updater implements Runnable {

        private MediaPlayer mPlayer;

        public Updater(MediaPlayer p) {
            this.mPlayer = p;
        }

        public void run() {
            Platform.runLater(() -> {
                if (!isDragging)
                    slider.setValue(mPlayer.getPosition());
            });
        }
    }

}
