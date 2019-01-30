package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MyPickerView extends View {
    public static final float MARGIN_ALPHA = 2.8f;
    public static final float SPEED = 2.0f;
    public static final String TAG = "PickerView";
    private ListViewInterface iface = null;
    private boolean isInit = false;
    private Bitmap mBmp = null;
    private int mColorText = ViewCompat.MEASURED_SIZE_MASK;
    private int mCurrentSelected;
    private ArrayList<HashMap<String, Object>> mDataList;
    private float mLastDownY;
    private float mMaxTextAlpha = 255.0f;
    private float mMaxTextSize = 30.0f;
    private float mMinTextAlpha = 255.0f;
    private float mMinTextSize = 20.0f;
    private float mMoveLen = 0.0f;
    private Paint mPaint;
    private onSelectListener mSelectListener;
    private MyTimerTask mTask;
    private int mViewHeight;
    private int mViewWidth;
    private Timer timer;
    private Typeface typeFace = null;
    Handler updateHandler = new C04351();

    /* renamed from: com.gemini.play.MyPickerView$1 */
    class C04351 extends Handler {
        C04351() {
        }

        public void handleMessage(Message msg) {
            if (Math.abs(MyPickerView.this.mMoveLen) < 2.0f) {
                MyPickerView.this.mMoveLen = 0.0f;
                if (MyPickerView.this.mTask != null) {
                    MyPickerView.this.mTask.cancel();
                    MyPickerView.this.mTask = null;
                    MyPickerView.this.performSelect();
                }
            } else {
                MyPickerView.this.mMoveLen = MyPickerView.this.mMoveLen - ((MyPickerView.this.mMoveLen / Math.abs(MyPickerView.this.mMoveLen)) * 2.0f);
            }
            MyPickerView.this.invalidate();
        }
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        public void run() {
            this.handler.sendMessage(this.handler.obtainMessage());
        }
    }

    public interface onSelectListener {
        void onSelect(String str);
    }

    public MyPickerView(Context context) {
        super(context);
        init();
    }

    public MyPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        this.mSelectListener = listener;
    }

    private void performSelect() {
        if (this.mSelectListener != null) {
            this.mSelectListener.onSelect(((HashMap) this.mDataList.get(this.mCurrentSelected)).get("ItemID").toString());
        }
    }

    public void setData(ArrayList<HashMap<String, Object>> datas) {
        this.mDataList = datas;
        this.mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    public void setSelected(int selected) {
        this.mCurrentSelected = selected;
    }

    private void moveHeadToTail() {
        HashMap<String, Object> head = (HashMap) this.mDataList.get(0);
        this.mDataList.remove(0);
        this.mDataList.add(head);
    }

    private void moveTailToHead() {
        HashMap<String, Object> tail = (HashMap) this.mDataList.get(this.mDataList.size() - 1);
        this.mDataList.remove(this.mDataList.size() - 1);
        this.mDataList.add(0, tail);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mViewHeight = getMeasuredHeight();
        this.mViewWidth = getMeasuredWidth();
        this.isInit = true;
        invalidate();
    }

    private void init() {
        this.timer = new Timer();
        this.mDataList = new ArrayList();
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setTextAlign(Align.CENTER);
        this.mPaint.setColor(this.mColorText);
        this.mBmp = BitmapFactory.decodeResource(getResources(), C0216R.mipmap.se);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isInit) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        int i;
        float scale = parabola(((float) this.mViewHeight) / 4.0f, this.mMoveLen);
        this.mPaint.setTextSize(((this.mMaxTextSize - this.mMinTextSize) * scale) + this.mMinTextSize);
        this.mPaint.setAlpha((int) (((this.mMaxTextAlpha - this.mMinTextAlpha) * scale) + this.mMinTextAlpha));
        if (this.typeFace != null) {
            this.mPaint.setTypeface(this.typeFace);
        }
        float x = (float) (((double) this.mViewWidth) / 2.0d);
        float y = (float) ((((double) this.mViewHeight) / 2.0d) + ((double) this.mMoveLen));
        FontMetricsInt fmi = this.mPaint.getFontMetricsInt();
        float baseline = (float) (((double) y) - ((((double) fmi.bottom) / 2.0d) + (((double) fmi.top) / 2.0d)));
        this.mBmp = BitmapFactory.decodeResource(getResources(), C0216R.mipmap.se);
        drawImage(canvas, this.mBmp, this.mPaint, 0, (int) (y - ((float) ((fmi.bottom - fmi.top) / 2))), this.mViewWidth, fmi.bottom - fmi.top, 0, 0);
        canvas.drawText(((HashMap) this.mDataList.get(this.mCurrentSelected)).get("ItemName").toString(), x, baseline, this.mPaint);
        for (i = 1; this.mCurrentSelected - i >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        for (i = 1; this.mCurrentSelected + i < this.mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = ((2.8f * this.mMinTextSize) * ((float) position)) + (((float) type) * this.mMoveLen);
        float scale = parabola(((float) this.mViewHeight) / 4.0f, d);
        this.mPaint.setTextSize(((this.mMaxTextSize - this.mMinTextSize) * scale) + this.mMinTextSize);
        this.mPaint.setAlpha((int) (((this.mMaxTextAlpha - this.mMinTextAlpha) * scale) + this.mMinTextAlpha));
        if (this.typeFace != null) {
            this.mPaint.setTypeface(this.typeFace);
        }
        float y = (float) ((((double) this.mViewHeight) / 2.0d) + ((double) (((float) type) * d)));
        FontMetricsInt fmi = this.mPaint.getFontMetricsInt();
        float baseline = (float) (((double) y) - ((((double) fmi.bottom) / 2.0d) + (((double) fmi.top) / 2.0d)));
        canvas.drawText(((HashMap) this.mDataList.get(this.mCurrentSelected + (type * position))).get("ItemName").toString(), (float) (((double) this.mViewWidth) / 2.0d), baseline, this.mPaint);
    }

    private float parabola(float zero, float x) {
        float f = (float) (1.0d - Math.pow((double) (x / zero), 2.0d));
        return f < 0.0f ? 0.0f : f;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 19) {
            doMoveLeft();
        } else if (keyCode == 20) {
            doMoveRight();
        } else if (keyCode == 23 || keyCode == 66) {
            this.iface.callback(0, ((HashMap) this.mDataList.get(this.mCurrentSelected)).get("ItemID").toString());
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
                doDown(event);
                break;
            case 1:
                doUp(event);
                break;
            case 2:
                doMove(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
        this.mLastDownY = event.getY();
    }

    private void doMoveLeft() {
        this.mCurrentSelected--;
        if (this.mCurrentSelected < 0) {
            this.mCurrentSelected = 0;
            return;
        }
        this.mMoveLen = 0.0f - (2.8f * this.mMinTextSize);
        this.mTask = new MyTimerTask(this.updateHandler);
        this.timer.schedule(this.mTask, 0, 10);
    }

    private void doMoveRight() {
        this.mCurrentSelected++;
        if (this.mCurrentSelected > this.mDataList.size() - 1) {
            this.mCurrentSelected = this.mDataList.size() - 1;
            return;
        }
        this.mMoveLen = 2.8f * this.mMinTextSize;
        this.mTask = new MyTimerTask(this.updateHandler);
        this.timer.schedule(this.mTask, 0, 10);
    }

    private void doMove(MotionEvent event) {
        this.mMoveLen += event.getY() - this.mLastDownY;
        if (this.mMoveLen > (this.mMinTextSize * 2.8f) / 2.0f) {
            moveTailToHead();
            this.mMoveLen -= this.mMinTextSize * 2.8f;
        } else if (this.mMoveLen < (-2.8f * this.mMinTextSize) / 2.0f) {
            moveHeadToTail();
            this.mMoveLen += this.mMinTextSize * 2.8f;
        }
        this.mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        if (((double) Math.abs(this.mMoveLen)) < 1.0E-4d) {
            this.mMoveLen = 0.0f;
            return;
        }
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
        this.mTask = new MyTimerTask(this.updateHandler);
        this.timer.schedule(this.mTask, 0, 10);
        this.iface.callback(0, ((HashMap) this.mDataList.get(this.mCurrentSelected)).get("ItemID").toString());
    }

    public void getFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
    }

    public void setFontSize(int size) {
        float f = (float) size;
        this.mMinTextSize = f;
        this.mMaxTextSize = f;
    }

    public void setTypeface(Typeface t) {
        this.typeFace = t;
    }

    public static void drawImage(Canvas canvas, Bitmap blt, Paint mPaint, int x, int y, int w, int h, int bx, int by) {
        Rect src = new Rect();
        Rect dst = new Rect();
        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;
        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        canvas.drawBitmap(blt, src, dst, mPaint);
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
