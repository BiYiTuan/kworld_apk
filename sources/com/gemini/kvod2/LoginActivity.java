package com.gemini.kvod2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import com.gemini.custom.quanxing;
import com.gemini.play.MGplayer;

public class LoginActivity extends Activity {
    private Button cancelbutton;
    private EditText passEdit;
    private Button savebutton;
    private EditText userEdit;

    /* renamed from: com.gemini.kvod2.LoginActivity$1 */
    class C02121 implements OnKeyListener {
        C02121() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case 23:
                case 66:
                    LoginActivity.this.sendCmd();
                    LoginActivity.this.finish();
                    return true;
                default:
                    return false;
            }
        }
    }

    /* renamed from: com.gemini.kvod2.LoginActivity$2 */
    class C02132 implements OnClickListener {
        C02132() {
        }

        public void onClick(View arg0) {
            LoginActivity.this.sendCmd();
            LoginActivity.this.finish();
        }
    }

    /* renamed from: com.gemini.kvod2.LoginActivity$3 */
    class C02143 implements OnKeyListener {
        C02143() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case 23:
                case 66:
                    LoginActivity.this.finish();
                    return true;
                default:
                    return false;
            }
        }
    }

    /* renamed from: com.gemini.kvod2.LoginActivity$4 */
    class C02154 implements OnClickListener {
        C02154() {
        }

        public void onClick(View arg0) {
            LoginActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0216R.layout.login);
        this.savebutton = (Button) findViewById(C0216R.id.button1);
        this.cancelbutton = (Button) findViewById(C0216R.id.button2);
        this.userEdit = (EditText) findViewById(C0216R.id.editText2);
        this.passEdit = (EditText) findViewById(C0216R.id.editText1);
        this.savebutton.setOnKeyListener(new C02121());
        this.savebutton.setOnClickListener(new C02132());
        this.cancelbutton.setOnKeyListener(new C02143());
        this.cancelbutton.setOnClickListener(new C02154());
    }

    private void sendCmd() {
        String user = this.userEdit.getText().toString();
        String pass = this.passEdit.getText().toString();
        SharedPreferences settings = MGplayer.MyGetSharedPreferences(this, "data", 0);
        String cmd = settings.getString("login_page", quanxing.login_url) + "?id=" + settings.getString("mac", "null") + "&user=" + user + "&pass=" + pass;
        Intent data = new Intent();
        data.putExtra("url", cmd);
        setResult(20, data);
    }
}
