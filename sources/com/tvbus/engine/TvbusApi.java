package com.tvbus.engine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.gemini.custom.jjm;
import com.gemini.play.MGplayer;
import cz.msebera.android.httpclient.HttpHost;
import org.json.JSONException;
import org.json.JSONObject;

public class TvbusApi {
    private static final String TAG = "TVBusActivity";
    private static TVCore mTVCore = null;
    private static String playbackUrl;
    private static boolean useMPEGTS = true;
    private Context _this = null;
    private int mBuffer;
    private int mDlRate;
    private int mDlTotal;
    private int mTmPlayerConn;
    private int mUlRate;
    private int mUlTotal;
    private Handler pHandler;

    /* renamed from: com.tvbus.engine.TvbusApi$1 */
    class C09241 implements TVListener {
        C09241() {
        }

        public void onInited(String result) {
            if (TvbusApi.this.parseCallbackInfo("onInited", result)) {
                TvbusApi.this.tvcoreInited();
            }
            MGplayer.MyPrintln("TVCore is inited ... " + result);
        }

        public void onStart(String result) {
            TvbusApi.this.parseCallbackInfo("onStart", result);
            MGplayer.MyPrintln("TVCore onStart ... " + result);
        }

        public void onPrepared(String result) {
            if (TvbusApi.this.parseCallbackInfo("onPrepared", result)) {
                TvbusApi.this.startPlayback();
            }
            MGplayer.MyPrintln("TVCore onPrepared ... " + result);
        }

        public void onInfo(String result) {
            Log.i(TvbusApi.TAG, "TVCore onInfo 1... " + result);
            if (TvbusApi.this.parseCallbackInfo("onInfo", result)) {
                MGplayer.MyPrintln("TVCore onInfo 2... " + result);
                TvbusApi.this.updateStatus();
                MGplayer.MyPrintln("TVCore onInfo 3... " + result);
            }
            MGplayer.MyPrintln("TVCore onInfo 4... " + result);
            TvbusApi.this.checkPlayer();
            MGplayer.MyPrintln("TVCore onInfo 5... " + result);
        }

        public void onStop(String result) {
            if (TvbusApi.this.parseCallbackInfo("onStop", result)) {
                TvbusApi.this.showCHResult();
            }
            MGplayer.MyPrintln("TVCore onStop ... " + result);
        }

        public void onQuit(String result) {
            MGplayer.MyPrintln("TVCore onQuit ... " + result);
        }
    }

    public void startTVBusService(Context t) {
        mTVCore = TVCore.getInstance();
        this._this = t;
        if (MGplayer.custom().equals("huanqiu")) {
            mTVCore.setMKBroker("http://mkb.dreamhdtv.com:3918/v1/caches");
            mTVCore.setAuthUrl("https://auth.dreamhdtv.com/v1/auth");
            mTVCore.setUsername(MGplayer.tv.GetMac());
            mTVCore.setPassword(MGplayer.tv.getCpuID());
        }
        String url = jjm.auth_url;
        if (MGplayer.custom().equals("jingjimu") || MGplayer.custom().equals("jingjimudev") || MGplayer.custom().equals("tvgo") || MGplayer.custom().equals("tvgo2")) {
            if (MGplayer.custom().equals("jingjimu") || MGplayer.custom().equals("tvgo") || MGplayer.custom().equals("tvgo2")) {
                if (MGplayer.tv.gete().contains("158.69.208.219") || MGplayer.tv.gete().contains("www.haofafa.com.tw") || MGplayer.tv.gete().contains("http://box.123178.net/proxy/") || MGplayer.custom().equals("tvgo") || MGplayer.custom().equals("tvgo2")) {
                    MGplayer.MyPrintln("auth v5");
                    mTVCore.setAuthUrl("https://auth.tvgood.taipei/v5/auth");
                } else if (MGplayer.tv.gete().contains("www.wordtv.com.tw") || MGplayer.tv.gete().contains("139.99.46.161")) {
                    MGplayer.MyPrintln("auth v3");
                    mTVCore.setAuthUrl("https://auth.tvgood.taipei/v3/auth");
                } else {
                    mTVCore.setAuthUrl("https://auth.tvgood.taipei/v1/auth");
                }
            } else if (MGplayer.custom().equals("jingjimudev")) {
                mTVCore.setAuthUrl("https://auth.tvgood.taipei/v2/auth");
            }
            mTVCore.setUsername(MGplayer.tv.GetMac());
            mTVCore.setPassword(MGplayer.tv.getCpuID());
        }
        if (MGplayer.custom().equals("pandaiptv")) {
            mTVCore.setAuthUrl("https://auth.tvgood.taipei/v2/auth");
            mTVCore.setUsername(MGplayer.tv.GetMac());
            mTVCore.setPassword(MGplayer.tv.getCpuID());
        }
        mTVCore.setTVListener(new C09241());
        this._this.startService(new Intent(this._this, TVService.class));
    }

    public boolean parseCallbackInfo(String callback, String result) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObj == null) {
            return false;
        }
        if (callback.equals("onInited")) {
            if (jsonObj.optInt("tvcore", 1) != 1) {
                return true;
            }
            return false;
        } else if (callback.equals("onStart")) {
            jsonObj.optString("mkcache", "");
            return true;
        } else if (callback.equals("onPrepared")) {
            Message msg;
            Bundle data;
            if (useMPEGTS && jsonObj.optString(HttpHost.DEFAULT_SCHEME_NAME, null) != null) {
                playbackUrl = jsonObj.optString(HttpHost.DEFAULT_SCHEME_NAME, null);
                MGplayer.MyPrintln("TVCore playbackUrl ... " + playbackUrl);
                msg = new Message();
                data = new Bundle();
                data.putString("url", playbackUrl);
                msg.setData(data);
                msg.what = 91;
                if (this.pHandler.hasMessages(91)) {
                    this.pHandler.removeMessages(91);
                }
                this.pHandler.sendMessage(msg);
                return true;
            } else if (useMPEGTS || jsonObj.optString("hls", null) == null) {
                return false;
            } else {
                playbackUrl = jsonObj.optString("hls", null);
                MGplayer.MyPrintln("TVCore playbackUrl ... " + playbackUrl);
                msg = new Message();
                data = new Bundle();
                data.putString("url", playbackUrl);
                msg.setData(data);
                msg.what = 91;
                if (this.pHandler.hasMessages(91)) {
                    this.pHandler.removeMessages(91);
                }
                this.pHandler.sendMessage(msg);
                return true;
            }
        } else if (callback.equals("onInfo")) {
            this.mBuffer = jsonObj.optInt("buffer", 0);
            this.mDlRate = jsonObj.optInt("download_rate", 0);
            this.mUlRate = jsonObj.optInt("upload_rate", 0);
            this.mDlTotal = jsonObj.optInt("download_total", 0);
            this.mUlTotal = jsonObj.optInt("upload_total", 0);
            this.mTmPlayerConn = jsonObj.optInt("hls_last_conn", 0);
            return true;
        } else if (callback.equals("onStop")) {
            if (jsonObj.optInt("errno", 1) != 1) {
                return true;
            }
            return false;
        } else if (callback.equals("onQut")) {
            return true;
        } else {
            return false;
        }
    }

    public void stopTvBusService() {
        this._this.stopService(new Intent(this._this, TVService.class));
    }

    public void stopChannel() {
        mTVCore.stop();
    }

    public void startChannel(String address, Handler h) {
        stoPlayback();
        System.currentTimeMillis();
        MGplayer.MyPrintln("TVCore startChannel:" + address);
        mTVCore.start(address);
        this.pHandler = h;
    }

    public void stoPlayback() {
    }

    public void startPlayback() {
    }

    public void tvcoreInited() {
    }

    public void showCHResult() {
    }

    public void updateStatus() {
    }

    public void checkPlayer() {
    }
}
