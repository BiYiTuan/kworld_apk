package com.gemini.play;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;

public class MyToast {
    static TextView message = null;
    static Toast toastStart = null;

    public static void makeText(Context mContext, String m, int duration) {
        View toastRoot = ((LayoutInflater) mContext.getSystemService("layout_inflater")).inflate(C0216R.layout.toast, null);
        message = (TextView) toastRoot.findViewById(C0216R.id.message);
        message.setText(m);
        toastStart = new Toast(mContext);
        toastStart.setGravity(17, 0, 0);
        toastStart.setDuration(duration);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    static void show() {
        if (toastStart != null) {
            toastStart.show();
        }
    }

    static void hide() {
    }
}
