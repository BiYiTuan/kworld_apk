package com.gemini.play;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public class ScreenObserver {
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver = new ScreenBroadcastReceiver();
    private ScreenStateListener mScreenStateListener;

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action;

        private ScreenBroadcastReceiver() {
            this.action = null;
        }

        public void onReceive(Context context, Intent intent) {
            this.action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(this.action)) {
                ScreenObserver.this.mScreenStateListener.onScreenOn();
            } else if ("android.intent.action.SCREEN_OFF".equals(this.action)) {
                ScreenObserver.this.mScreenStateListener.onScreenOff();
            } else if ("android.intent.action.USER_PRESENT".equals(this.action)) {
                ScreenObserver.this.mScreenStateListener.onUserPresent();
            }
        }
    }

    public interface ScreenStateListener {
        void onScreenOff();

        void onScreenOn();

        void onUserPresent();
    }

    public ScreenObserver(Context context) {
        this.mContext = context;
    }

    public void startObserver(ScreenStateListener listener) {
        this.mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    public void shutdownObserver() {
        unregisterListener();
    }

    @SuppressLint({"NewApi"})
    private void getScreenState() {
        if (this.mContext != null) {
            if (((PowerManager) this.mContext.getSystemService("power")).isScreenOn()) {
                if (this.mScreenStateListener != null) {
                    this.mScreenStateListener.onScreenOn();
                }
            } else if (this.mScreenStateListener != null) {
                this.mScreenStateListener.onScreenOff();
            }
        }
    }

    private void registerListener() {
        if (this.mContext != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.USER_PRESENT");
            this.mContext.registerReceiver(this.mScreenReceiver, filter);
        }
    }

    private void unregisterListener() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mScreenReceiver);
        }
    }
}
