package com.gemini.play;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.BaseSavedState;
import android.view.WindowManager;
import android.widget.TextView;

public class ScrollTextView extends TextView {
    public static final String TAG = ScrollTextView.class.getSimpleName();
    private static int time = 0;
    private String color = "FFFFFF";
    private boolean first = true;
    public boolean isStarting = false;
    private Paint paint = null;
    private int settime = -1;
    private float speed = 2.0f;
    private float step = 0.0f;
    private float temp_view_plus_text_length = 0.0f;
    private float temp_view_plus_two_text_length = 0.0f;
    private String text = "";
    private float textLength = 0.0f;
    private String text_dtime = null;
    private String text_every = null;
    private String text_one = null;
    private int times_dtime = 0;
    private int times_every = -2;
    private int times_one = -2;
    private int times_runtime = 0;
    private float viewWidth = 0.0f;
    /* renamed from: x */
    private float f15x = 0.0f;
    /* renamed from: y */
    private float f16y = 0.0f;

    /* renamed from: com.gemini.play.ScrollTextView$1 */
    class C05581 implements Runnable {
        C05581() {
        }

        public void run() {
            ScrollTextView.this.invalidate();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C05591();
        public boolean isStarting;
        public float step;

        /* renamed from: com.gemini.play.ScrollTextView$SavedState$1 */
        static class C05591 implements Creator<SavedState> {
            C05591() {
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
            this.isStarting = false;
            this.step = 0.0f;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{this.isStarting});
            out.writeFloat(this.step);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isStarting = false;
            this.step = 0.0f;
            boolean[] b = null;
            in.readBooleanArray(b);
            if (b != null && b.length > 0) {
                this.isStarting = b[0];
            }
            this.step = in.readFloat();
        }
    }

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(WindowManager windowManager) {
        this.paint = getPaint();
        char[] c = this.color.toLowerCase().toCharArray();
        if (c.length < 6) {
            this.paint.setARGB(255, 255, 255, 255);
        } else if (c.length == 6) {
            this.paint.setARGB(255, char_to_int(c[0]) * char_to_int(c[1]), char_to_int(c[2]) * char_to_int(c[3]), char_to_int(c[4]) * char_to_int(c[5]));
        } else {
            this.paint.setARGB(255, 255, 255, 255);
        }
        this.text = getText().toString();
        this.textLength = this.paint.measureText(this.text);
        this.viewWidth = (float) getWidth();
        if (this.viewWidth == 0.0f && windowManager != null) {
            this.viewWidth = (float) windowManager.getDefaultDisplay().getWidth();
        }
        if (this.textLength > this.viewWidth) {
            this.isStarting = true;
        }
        this.step = this.textLength;
        this.temp_view_plus_text_length = this.viewWidth + this.textLength;
        this.temp_view_plus_two_text_length = this.viewWidth + (this.textLength * 2.0f);
        this.f16y = getTextSize() + ((float) getPaddingTop());
        this.f15x = (float) getPaddingLeft();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.step = this.step;
        ss.isStarting = this.isStarting;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.step = ss.step;
            this.isStarting = ss.isStarting;
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void startScroll() {
        this.isStarting = true;
        invalidate();
    }

    public void stopScroll() {
        this.isStarting = false;
        invalidate();
    }

    public void hideScroll() {
        setVisibility(8);
    }

    public void setScrollTime(int value) {
        this.settime = value;
    }

    public void resetTime() {
        time = 0;
    }

    public void speedScroll(float value) {
        this.speed = value;
    }

    public void setColor(String value) {
        this.color = value;
    }

    public void onDraw(Canvas canvas) {
        int dtimeout = 0;
        if (this.first) {
            this.viewWidth = (float) getWidth();
            Log.i("width", "width=" + this.viewWidth);
            this.first = false;
        }
        if (this.speed == 0.0f) {
            canvas.drawText(this.text, 0.0f, this.f16y, this.paint);
        }
        if (this.isStarting) {
            if (this.paint != null) {
                canvas.drawText(this.text, this.temp_view_plus_text_length - this.step, this.f16y, this.paint);
            }
            this.step += this.speed;
            if (this.step > this.temp_view_plus_two_text_length) {
                time++;
                this.step = this.textLength;
                if (this.times_dtime > 0) {
                    dtimeout = 1;
                }
                MGplayer.MyPrintln("times_dtime = " + this.times_dtime);
            } else {
                dtimeout = 0;
            }
            if (this.settime > 0 && time > this.settime) {
                if (this.times_every != -3 || this.text_one == null) {
                    resetTime();
                    stopScroll();
                    hideScroll();
                } else {
                    this.text = this.text_one;
                    this.settime = this.times_one;
                    time = this.times_runtime;
                    this.times_every = -2;
                }
            }
            if (this.text_every != null && this.times_every > -2) {
                this.times_runtime = time;
                this.text = this.text_every;
                this.settime = this.times_every;
                time = 0;
                this.text_every = null;
                this.times_every = -3;
            }
            if (dtimeout == 0) {
                invalidate();
            } else {
                new Handler().postDelayed(new C05581(), (long) (this.times_dtime * 1000));
            }
        } else if (this.paint != null) {
            canvas.drawText(this.text, this.f15x, this.f16y, this.paint);
        }
    }

    public int gettime() {
        return time;
    }

    private int char_to_int(char c) {
        if (c == '0') {
            return 0;
        }
        if (c == '1') {
            return 1;
        }
        if (c == '2') {
            return 2;
        }
        if (c == '3') {
            return 3;
        }
        if (c == '4') {
            return 4;
        }
        if (c == '5') {
            return 5;
        }
        if (c == '6') {
            return 6;
        }
        if (c == '7') {
            return 7;
        }
        if (c == '8') {
            return 8;
        }
        if (c == '9') {
            return 9;
        }
        if (c == 'a') {
            return 10;
        }
        if (c == 'b') {
            return 11;
        }
        if (c == 'c') {
            return 12;
        }
        if (c == 'd') {
            return 13;
        }
        if (c == 'e') {
            return 14;
        }
        if (c == 'f') {
            return 15;
        }
        return 0;
    }

    public void start(Activity context, String str, int x0, int y0, int w0, int h0, float speed, int fontsize, String color) {
        setVisibility(0);
        setTextSize((float) fontsize);
        speedScroll(speed);
        setColor(color);
        setText(str);
        init(context.getWindowManager());
        startScroll();
    }

    public void start_every(Activity context, String str, int x, int y, int w, int h, float speed, int fontsize, String color, int time) {
        this.text_every = str;
        this.times_every = time;
        stopScroll();
        MGplayer.MyPrintln("start_every:" + this.text_every + " " + this.times_every);
        setVisibility(0);
        setTextSize((float) fontsize);
        speedScroll(speed);
        setColor(color);
        init(context.getWindowManager());
        startScroll();
    }

    public void start(Activity context, String str, int x, int y, int w, int h, float speed, int fontsize, String color, int time) {
        this.text_one = str;
        this.times_one = time;
        setVisibility(0);
        setTextSize((float) fontsize);
        speedScroll(speed);
        setColor(color);
        setText(str);
        if (this.times_one > 0) {
            setScrollTime(this.times_one);
        }
        init(context.getWindowManager());
        startScroll();
    }

    public void start_dtime(Activity context, String str, int x, int y, int w, int h, float speed, int fontsize, String color, int dtime) {
        this.text_dtime = str;
        this.times_dtime = dtime;
        setVisibility(0);
        setTextSize((float) fontsize);
        speedScroll(speed);
        setColor(color);
        setText(str);
        init(context.getWindowManager());
        startScroll();
    }
}
