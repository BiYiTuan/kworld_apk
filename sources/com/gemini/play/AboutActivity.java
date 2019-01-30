package com.gemini.play;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class AboutActivity extends Activity {
    int fontsize = 8;

    /* renamed from: com.gemini.play.AboutActivity$1 */
    class C02391 implements OnClickListener {
        C02391() {
        }

        public void onClick(View v) {
            AboutActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MGplayer.custom().equals("szysx") || MGplayer.custom().equals("dhtv") || MGplayer.custom().equals("familytv")) {
            setContentView(C0216R.layout.about_szysx);
        } else if (MGplayer.custom().equals("saiptv")) {
            setContentView(C0216R.layout.about2);
        } else {
            setContentView(C0216R.layout.about);
        }
        getWindow().setFlags(1024, 1024);
        Typeface typeFace = MGplayer.getFontsType(this);
        float rate = MGplayer.getFontsRate();
        ((TextView) findViewById(C0216R.id.textView1)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView2)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView3)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView4)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView5)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView6)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView7)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView8)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView9)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView10)).setTextSize(((float) this.fontsize) * rate);
        ((TextView) findViewById(C0216R.id.textView1)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView2)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView3)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView4)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView5)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView6)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView7)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView8)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView9)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView10)).setTypeface(typeFace);
        ((TextView) findViewById(C0216R.id.textView7)).setText("MAC:" + MGplayer.tv.GetMac());
        ((TextView) findViewById(C0216R.id.textView8)).setText("CPUID:" + MGplayer.tv.getCpuID());
        ((TextView) findViewById(C0216R.id.textView9)).setText(getString(C0216R.string.about_text2).toString() + ":" + MGplayer.getVersion());
        ((TextView) findViewById(C0216R.id.textView10)).setText(getString(C0216R.string.about_text1).toString() + ":" + MGplayer.number);
        Button cancelButton = (Button) findViewById(C0216R.id.negativeButton);
        cancelButton.setTextSize(((float) this.fontsize) * rate);
        cancelButton.setTypeface(typeFace);
        cancelButton.setOnClickListener(new C02391());
    }
}
