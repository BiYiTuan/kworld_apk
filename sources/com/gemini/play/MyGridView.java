package com.gemini.play;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isInTouchMode() {
        if (19 == VERSION.SDK_INT) {
            return !hasFocus() || super.isInTouchMode();
        } else {
            return super.isInTouchMode();
        }
    }
}
