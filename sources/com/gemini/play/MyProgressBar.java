package com.gemini.play;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {
    private Paint mPaint;
    private String text_progress = "";

    public MyProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    public void setProgressText(String text) {
        this.text_progress = text;
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text_progress, 0, this.text_progress.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        this.mPaint.setTextSize(24.0f);
        canvas.drawText(this.text_progress, (float) x, (float) y, this.mPaint);
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-1);
    }

    private void setTextProgress(int progress) {
        if (progress < 0) {
            setVisibility(8);
            return;
        }
        this.text_progress = String.valueOf((int) (((((float) progress) * 1.0f) / ((float) getMax())) * 100.0f)) + "%";
    }
}
