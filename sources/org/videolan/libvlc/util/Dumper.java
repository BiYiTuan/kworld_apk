package org.videolan.libvlc.util;

import android.net.Uri;
import android.support.annotation.MainThread;
import java.util.ArrayList;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.MediaPlayer.Event;
import org.videolan.libvlc.MediaPlayer.EventListener;

public class Dumper {
    private final LibVLC mLibVLC;
    private final Listener mListener;
    private final MediaPlayer mMediaPlayer;

    public interface Listener {
        void onFinish(boolean z);

        void onProgress(float f);
    }

    /* renamed from: org.videolan.libvlc.util.Dumper$1 */
    class C09901 implements EventListener {
        C09901() {
        }

        public void onEvent(Event event) {
            switch (event.type) {
                case Event.Buffering /*259*/:
                    Dumper.this.mListener.onProgress(event.getBuffering());
                    return;
                case Event.EndReached /*265*/:
                case Event.EncounteredError /*266*/:
                    Dumper.this.mListener.onFinish(event.type == Event.EndReached);
                    Dumper.this.cancel();
                    return;
                default:
                    return;
            }
        }
    }

    @MainThread
    public Dumper(Uri uri, String filepath, Listener listener) {
        if (uri == null || filepath == null || listener == null) {
            throw new IllegalArgumentException("arguments shouldn't be null");
        }
        this.mListener = listener;
        ArrayList<String> options = new ArrayList(8);
        options.add("--demux");
        options.add("dump2,none");
        options.add("--demuxdump-file");
        options.add(filepath);
        options.add("--no-video");
        options.add("--no-audio");
        options.add("--no-spu");
        options.add("-vvv");
        this.mLibVLC = new LibVLC(null, options);
        Media media = new Media(this.mLibVLC, uri);
        this.mMediaPlayer = new MediaPlayer(media);
        this.mMediaPlayer.setEventListener(new C09901());
        media.release();
    }

    @MainThread
    public void start() {
        this.mMediaPlayer.play();
    }

    @MainThread
    public void cancel() {
        this.mMediaPlayer.stop();
        this.mMediaPlayer.release();
        this.mLibVLC.release();
    }
}
