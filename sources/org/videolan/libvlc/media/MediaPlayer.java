package org.videolan.libvlc.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaFormat;
import android.media.TimedText;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

public class MediaPlayer {
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
    private Media mCurrentMedia = null;
    private final LibVLC mLibVLC = new LibVLC(null);
    private org.videolan.libvlc.MediaPlayer mMediaPlayer = new org.videolan.libvlc.MediaPlayer(this.mLibVLC);

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);
    }

    public interface OnCompletionListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnInfoListener {
        boolean onInfo(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnPreparedListener {
        void onPrepared(MediaPlayer mediaPlayer);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(MediaPlayer mediaPlayer);
    }

    public interface OnTimedTextListener {
        void onTimedText(MediaPlayer mediaPlayer, TimedText timedText);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2);
    }

    public static class TrackInfo implements Parcelable {
        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
        public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;

        TrackInfo(Parcel in) {
        }

        public int getTrackType() {
            return 0;
        }

        public String getLanguage() {
            return "und";
        }

        public MediaFormat getFormat() {
            return null;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
        }

        public String toString() {
            return "";
        }
    }

    public static MediaPlayer create(Context context, Uri uri) {
        return create(context, uri, null);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
        return create(context, uri, holder, null, 0);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder, AudioAttributes audioAttributes, int audioSessionId) {
        return new MediaPlayer();
    }

    public static MediaPlayer create(Context context, int resid) {
        return create(context, resid, null, 0);
    }

    public static MediaPlayer create(Context context, int resid, AudioAttributes audioAttributes, int audioSessionId) {
        return null;
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, null);
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mCurrentMedia = new Media(this.mLibVLC, uri);
        this.mMediaPlayer.setMedia(this.mCurrentMedia);
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mCurrentMedia = new Media(this.mLibVLC, path);
        this.mMediaPlayer.setMedia(this.mCurrentMedia);
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        this.mCurrentMedia = new Media(this.mLibVLC, fd);
        this.mMediaPlayer.setMedia(this.mCurrentMedia);
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd);
    }

    public void prepare() throws IOException, IllegalStateException {
    }

    public void prepareAsync() {
        this.mCurrentMedia.addOption(":video-paused");
        this.mMediaPlayer.play();
    }

    public void setDisplay(SurfaceHolder sh) {
        this.mMediaPlayer.getVLCVout().setVideoSurface(sh.getSurface(), sh);
    }

    public void setSurface(Surface surface) {
        this.mMediaPlayer.getVLCVout().setVideoSurface(surface, null);
    }

    public void setVideoScalingMode(int mode) {
    }

    public void start() throws IllegalStateException {
        this.mMediaPlayer.play();
    }

    public void stop() throws IllegalStateException {
        this.mMediaPlayer.stop();
    }

    public void pause() throws IllegalStateException {
        this.mMediaPlayer.pause();
    }

    public void setWakeMode(Context context, int mode) {
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
    }

    public int getVideoWidth() {
        return -1;
    }

    public int getVideoHeight() {
        return -1;
    }

    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    public void seekTo(int msec) throws IllegalStateException {
    }

    public int getCurrentPosition() {
        return (int) this.mMediaPlayer.getTime();
    }

    public int getDuration() {
        return (int) this.mMediaPlayer.getLength();
    }

    public void setNextMediaPlayer(MediaPlayer next) {
    }

    public void release() {
        this.mMediaPlayer.release();
    }

    public void reset() {
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
    }

    public void setLooping(boolean looping) {
    }

    public boolean isLooping() {
        return false;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mMediaPlayer.setVolume((int) (((leftVolume + rightVolume) * 100.0f) / 2.0f));
    }

    public void setAudioSessionId(int sessionId) throws IllegalArgumentException, IllegalStateException {
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void attachAuxEffect(int effectId) {
    }

    public void setAuxEffectSendLevel(float level) {
    }

    public TrackInfo[] getTrackInfo() throws IllegalStateException {
        return new TrackInfo[1];
    }

    public void addTimedTextSource(String path, String mimeType) {
        this.mMediaPlayer.addSlave(0, path, false);
    }

    public void addTimedTextSource(Context context, Uri uri, String mimeType) {
        this.mMediaPlayer.addSlave(0, uri, false);
    }

    public void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
    }

    public void addTimedTextSource(FileDescriptor fd, long offset, long length, String mime) throws IllegalArgumentException, IllegalStateException {
    }

    public int getSelectedTrack(int trackType) throws IllegalStateException {
        return 0;
    }

    public void selectTrack(int index) throws IllegalStateException {
    }

    public void deselectTrack(int index) throws IllegalStateException {
    }

    protected void finalize() {
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
    }

    public void setOnTimedTextListener(OnTimedTextListener listener) {
    }

    public void setOnErrorListener(OnErrorListener listener) {
    }

    public void setOnInfoListener(OnInfoListener listener) {
    }
}
