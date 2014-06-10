package base;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;

public class VLCMediaPlayer extends EmbeddedMediaPlayerComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String vlcPath;

	public VLCMediaPlayer() {
		this.vlcPath = "/usr/bin/vlc";
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		getMediaPlayer().setVolume(100);
		
		getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventListener() {
			
			public void videoOutput(MediaPlayer arg0, int arg1) {
				
			}
			
			public void titleChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void timeChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			public void subItemPlayed(MediaPlayer arg0, int arg1) {
				
			}
			
			public void subItemFinished(MediaPlayer player, int arg1) {
			}
			
			public void stopped(MediaPlayer arg0) {
				
			}
			
			public void snapshotTaken(MediaPlayer arg0, String arg1) {
				
			}
			
			public void seekableChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void positionChanged(MediaPlayer arg0, float arg1) {
				
			}
			
			public void playing(MediaPlayer arg0) {
				
			}
			
			public void paused(MediaPlayer arg0) {
				
			}
			
			public void pausableChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void opening(MediaPlayer arg0) {
				
			}
			
			public void newMedia(MediaPlayer arg0) {
				
			}
			
			public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
				
			}
			
			public void mediaStateChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			public void mediaFreed(MediaPlayer arg0) {
				
			}
			
			public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
				
			}
			
			public void lengthChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			public void forward(MediaPlayer arg0) {
				
			}
			
			public void finished(MediaPlayer arg0) {
				System.out.println("NEXT");
				SidePanel.playNext();
			}
			
			public void error(MediaPlayer arg0) {
				
			}
			
			public void endOfSubItems(MediaPlayer arg0) {
				
			}
			
			public void buffering(MediaPlayer arg0, float arg1) {
				
			}
			
			public void backward(MediaPlayer arg0) {
				
			}
		});
	}

	public void play(String mediaURL) {
		getMediaPlayer().playMedia(mediaURL);
	}

}
