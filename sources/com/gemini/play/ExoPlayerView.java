package com.gemini.play;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder.Callback;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver.Listener;
import org.succlz123.okplayer.listener.CaptionListener;
import org.succlz123.okplayer.listener.OkPlayerListener;
import org.succlz123.okplayer.view.OkVideoView;

public class ExoPlayerView extends OkVideoView implements OkPlayerListener, CaptionListener, Callback, Listener {
    public ExoPlayerView(Context context) {
        super(context);
    }

    public ExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isPlaying() {
        if (getPlaybackState() == 4) {
            return true;
        }
        return false;
    }

    public void start() {
        setPlayWhenReady(true);
    }
}
