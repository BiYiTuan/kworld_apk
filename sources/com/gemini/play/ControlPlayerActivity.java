package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.videolan.vlc.VlcVideoView;

public class ControlPlayerActivity extends Activity {
    public static Activity instance;
    private String CurrentURL = null;
    public VideoView VideoViewH;
    private VlcVideoView VideoViewS;
    public ControlVideoInterface onControlVideo = new C09046();
    Handler pHandler = new C02717();

    /* renamed from: com.gemini.play.ControlPlayerActivity$1 */
    class C02661 implements OnCompletionListener {
        C02661() {
        }

        public void onCompletion(MediaPlayer mp) {
        }
    }

    /* renamed from: com.gemini.play.ControlPlayerActivity$7 */
    class C02717 extends Handler {
        C02717() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ControlPlayerActivity.this.stopVideo();
                    ControlPlayerActivity.this.playVideo(msg.getData().getString("data").trim());
                    return;
                case 1:
                    ControlPlayerActivity.this.stopVideo();
                    ControlPlayerActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.ControlPlayerActivity$8 */
    class C02728 implements OnClickListener {
        C02728() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            ControlPlayerActivity.this.stopVideo();
            ControlPlayerActivity.this.finish();
        }
    }

    /* renamed from: com.gemini.play.ControlPlayerActivity$9 */
    class C02739 implements OnClickListener {
        C02739() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.gemini.play.ControlPlayerActivity$6 */
    class C09046 implements ControlVideoInterface {
        C09046() {
        }

        public void callback(int cmd, String data) {
            Message msg;
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data + "=" + ControlPlayerActivity.this.CurrentURL);
                    if (!data.trim().equals(ControlPlayerActivity.this.CurrentURL)) {
                        msg = new Message();
                        Bundle d = new Bundle();
                        d.putString("data", data.trim());
                        msg.setData(d);
                        msg.what = 0;
                        ControlPlayerActivity.this.pHandler.sendMessage(msg);
                        return;
                    }
                    return;
                case 1:
                    msg = new Message();
                    msg.what = 1;
                    ControlPlayerActivity.this.pHandler.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.controlplayer);
        getWindow().setFlags(1024, 1024);
        instance = this;
        MGplayer.video_every_interface(this.onControlVideo);
        this.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
        this.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
        this.VideoViewH.setVisibility(8);
        this.VideoViewH.setOnCompletionListener(new C02661());
        String url = getIntent().getStringExtra("vod_url").trim();
        MGplayer.MyPrintln("ControlPlayerActivity url:" + url);
        if (url != null && url.length() > 6) {
            playVideo(url);
        } else if (MGplayer.Broadcast.length() > 6) {
            playVideo(MGplayer.Broadcast);
        } else {
            finish();
        }
    }

    private void playVideo(String data) {
        if (MGplayer.getDecode() == 0) {
            this.VideoViewH.setVisibility(0);
            if (this.VideoViewS != null) {
                this.VideoViewS.setVisibility(8);
            }
            playVideoForHard(this.VideoViewH, data);
            return;
        }
        if (this.VideoViewS != null) {
            this.VideoViewS.setVisibility(0);
        }
        this.VideoViewH.setVisibility(8);
        if (this.VideoViewS != null) {
            playVideoForSoft(this.VideoViewS, data);
        }
    }

    private void stopVideo() {
        if (MGplayer.getDecode() == 0) {
            stopVideoForHard(this.VideoViewH);
        } else if (this.VideoViewS != null) {
            stopVideoForSoft(this.VideoViewS);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                exitActivity();
                return true;
            case 82:
                MenuView.gridMenuInit(this);
                MenuView.showAlertDialog(this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void stopVideoForSoft(VlcVideoView mVideoView) {
        if (this.CurrentURL != null && isUseHlsPlugin(this.CurrentURL)) {
            MGplayer.mediaplayerunload();
        }
        mVideoView.pause();
    }

    public void stopVideoForHard(VideoView mVideoView) {
        if (this.CurrentURL != null && isUseHlsPlugin(this.CurrentURL)) {
            MGplayer.mediaplayerunload();
        }
        mVideoView.pause();
        mVideoView.stopPlayback();
    }

    public void playVideoForSoft(final VlcVideoView mVideoView, final String url) {
        mVideoView.pause();
        if (url != null && isUseHlsPlugin(url)) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        playVideoMessage(url, new Handler() {
            public void handleMessage(Message msg) {
                String murl = null;
                switch (msg.what) {
                    case 91:
                    case 92:
                        murl = msg.getData().getString("url");
                        break;
                }
                if (murl != null) {
                    mVideoView.startPlay(murl);
                    ControlPlayerActivity.this.CurrentURL = url;
                }
            }
        });
    }

    public void playVideoForHard(final VideoView mVideoView, final String url) {
        mVideoView.pause();
        mVideoView.stopPlayback();
        if (url != null && isUseHlsPlugin(url)) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        playVideoMessage(url, new Handler() {
            public void handleMessage(Message msg) {
                String murl = null;
                switch (msg.what) {
                    case 91:
                    case 92:
                        murl = msg.getData().getString("url");
                        break;
                }
                if (murl != null) {
                    mVideoView.setVideoPath(murl);
                    mVideoView.start();
                    ControlPlayerActivity.this.CurrentURL = url;
                }
            }
        });
    }

    public void playVideoMessage(final String url, final Handler pHandler) {
        if (!isUseHlsPlugin(url)) {
            new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", url);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (isUseHlsPlugin(url)) {
            new Thread(new Runnable() {
                public void run() {
                    String cpuinfo = MGplayer.getCpuName();
                    int gplayer_port = MGplayer.mediaplayerload(url, 10, 0, 0);
                    MGplayer.MyPrintln("#################### port: " + gplayer_port + "####################");
                    if (gplayer_port < 0) {
                        MGplayer.mediaplayerunload();
                        Message msg = new Message();
                        msg.what = 94;
                        if (pHandler.hasMessages(94)) {
                            pHandler.removeMessages(94);
                        }
                        pHandler.sendMessage(msg);
                        return;
                    }
                    Message msg4 = new Message();
                    Bundle data = new Bundle();
                    if (cpuinfo.equals("AML8726")) {
                        data.putString("url", ControlPlayerActivity.createPlaylist(gplayer_port));
                    } else if (cpuinfo.equals("HI3716M") || cpuinfo.equals("HIK3V2")) {
                        data.putString("url", ControlPlayerActivity.createPlaylist(gplayer_port));
                    } else if (cpuinfo.equals("RK3128") || cpuinfo.equals("S805")) {
                        data.putString("url", "http://127.0.0.1:" + gplayer_port);
                    } else {
                        data.putString("url", "http://127.0.0.1:" + gplayer_port);
                    }
                    msg4.setData(data);
                    msg4.what = 92;
                    if (pHandler.hasMessages(92)) {
                        pHandler.removeMessages(92);
                    }
                    pHandler.sendMessage(msg4);
                }
            }).start();
        }
    }

    private static String createPlaylist(int gplayer_port) {
        try {
            FileOutputStream outStream = MGplayer._this.openFileOutput("playlist.m3u8", 1);
            try {
                outStream.write(("#EXTM3U\n#EXT-X-ALLOW-CACHE:YES\n#EXT-X-TARGETDURATION:72000\n#EXT-X-MEDIA-SEQUENCE:110236\n#EXTINF:1,\nhttp://127.0.0.1:" + Integer.toString(gplayer_port) + "/video.ts\n").getBytes());
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        return MGplayer._this.getFilesDir() + "/playlist.m3u8";
    }

    public boolean isUseHlsPlugin(String url) {
        if (url == null) {
            return false;
        }
        String cpuinfo = MGplayer.getCpuName();
        if (url.startsWith("rtsp://")) {
            return false;
        }
        if (url.startsWith("gemini://") || url.startsWith("gp2p://")) {
            return true;
        }
        if (cpuinfo.equals("S805")) {
            if (url.startsWith("rtmp://") || url.startsWith("udp://")) {
                return false;
            }
            if (url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) {
                return true;
            }
            if (!url.startsWith("http://") || url.contains("playlist.m3u8")) {
                return true;
            }
            return false;
        } else if (cpuinfo.equals("RK3128")) {
            if (url.startsWith("rtmp://") || url.startsWith("udp://")) {
                return false;
            }
            if (url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) {
                return false;
            }
            if (!url.startsWith("http://") || url.contains("playlist.m3u8")) {
                return true;
            }
            return false;
        } else if (cpuinfo.equals("HIK3V2")) {
            if (url.startsWith("rtmp://")) {
                return false;
            }
            if (url.startsWith("udp://")) {
                return true;
            }
            if (url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) {
                return false;
            }
            if (url.startsWith("http://") && !url.contains("playlist.m3u8")) {
                return false;
            }
            if (url.startsWith("http://") && !url.contains(":1935") && url.contains("playlist.m3u8")) {
                return false;
            }
            return true;
        } else if (url.startsWith("http://")) {
            return false;
        } else {
            return true;
        }
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C02728());
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new C02739());
        builder.create().show();
    }
}
