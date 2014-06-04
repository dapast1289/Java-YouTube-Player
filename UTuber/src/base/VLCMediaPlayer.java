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
			
			@Override
			public void videoOutput(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void titleChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void timeChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			@Override
			public void subItemPlayed(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void subItemFinished(MediaPlayer player, int arg1) {
			}
			
			@Override
			public void stopped(MediaPlayer arg0) {
				
			}
			
			@Override
			public void snapshotTaken(MediaPlayer arg0, String arg1) {
				
			}
			
			@Override
			public void seekableChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void positionChanged(MediaPlayer arg0, float arg1) {
				
			}
			
			@Override
			public void playing(MediaPlayer arg0) {
				
			}
			
			@Override
			public void paused(MediaPlayer arg0) {
				
			}
			
			@Override
			public void pausableChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void opening(MediaPlayer arg0) {
				
			}
			
			@Override
			public void newMedia(MediaPlayer arg0) {
				
			}
			
			@Override
			public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
				
			}
			
			@Override
			public void mediaStateChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void mediaFreed(MediaPlayer arg0) {
				
			}
			
			@Override
			public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			@Override
			public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
				
			}
			
			@Override
			public void lengthChanged(MediaPlayer arg0, long arg1) {
				
			}
			
			@Override
			public void forward(MediaPlayer arg0) {
				
			}
			
			@Override
			public void finished(MediaPlayer arg0) {
				System.out.println("NEXT");
				SidePanel.playNext();
			}
			
			@Override
			public void error(MediaPlayer arg0) {
				
			}
			
			@Override
			public void endOfSubItems(MediaPlayer arg0) {
				
			}
			
			@Override
			public void buffering(MediaPlayer arg0, float arg1) {
				
			}
			
			@Override
			public void backward(MediaPlayer arg0) {
				
			}
		});
	}

	public void play(String mediaURL) {
		getMediaPlayer().playMedia(mediaURL);
		System.out.println("Seekable: " + getMediaPlayer().isSeekable());
		
	}

}
