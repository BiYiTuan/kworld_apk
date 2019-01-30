package com.gemini.play;

import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

public class MGvalue {
    public String value_1(String msg) {
        String value = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).getString(msg, "error");
        MGplayer.MyPrintln(msg + " = " + value);
        return value;
    }

    public void value_2(String msg1, String msg2) {
        Editor editor = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).edit();
        editor.putString(msg1, msg2);
        editor.commit();
    }

    public String value_5() {
        String packageNames = "com.gemini.iptv";
        try {
            return MGplayer._this.getPackageManager().getPackageInfo(MGplayer._this.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return packageNames;
        }
    }
}
