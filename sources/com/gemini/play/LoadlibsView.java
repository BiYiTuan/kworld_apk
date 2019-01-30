package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.webkit.URLUtil;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyProgressDialog.Builder;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import net.lingala.zip4j.util.InternalZipConstants;

public class LoadlibsView {
    private static Activity _t;
    private static MyProgressDialog dialog;
    private static MyProgressDialog dialog2;
    private static Builder pDialog;
    private static Builder pDialog2;
    public static Handler rHandler = new C03133();
    private static boolean running = true;

    /* renamed from: com.gemini.play.LoadlibsView$1 */
    static class C03101 implements OnClickListener {
        C03101() {
        }

        public void onClick(DialogInterface dialog, int which) {
            LoadlibsView.running = false;
            dialog.dismiss();
        }
    }

    /* renamed from: com.gemini.play.LoadlibsView$3 */
    static class C03133 extends Handler {

        /* renamed from: com.gemini.play.LoadlibsView$3$1 */
        class C03121 implements OnClickListener {
            C03121() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
                LoadlibsView._t.stopService(new Intent(LoadlibsView._t, LocalService.class));
                LoadlibsView._t.finish();
                Process.killProcess(Process.myPid());
                System.exit(0);
                dialog.dismiss();
            }
        }

        C03133() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    new MyDialog2.Builder(LoadlibsView._t).setTitle(LoadlibsView._t.getString(C0216R.string.validate_text1).toString()).setMessage(LoadlibsView._t.getString(C0216R.string.loadlibsview_text2).toString()).setPositiveButton(17039370, new C03121()).create().show();
                    return;
                default:
                    return;
            }
        }
    }

    public static void showDownload(Activity t, String strUrl, String md5, String osfile, int version) {
        running = true;
        _t = t;
        pDialog = new Builder(t);
        dialog = pDialog.setMessage(t.getString(C0216R.string.loadlibsview_text1).toString()).setNegativeButton(17039360, new C03101()).create();
        dialog.show();
        final Activity activity = t;
        final String str = strUrl;
        final String str2 = osfile;
        final String str3 = md5;
        final int i = version;
        new Thread() {
            public void run() {
                try {
                    LoadlibsView.getDataSource(activity, str, str2, str3, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void getDataSource(Activity t, String strPath, String sofile, String ter_md5, int version) throws Exception {
        if (URLUtil.isNetworkUrl(strPath)) {
            URLConnection conn = new URL(strPath).openConnection();
            conn.connect();
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                MGplayer.MyPrintln("LoadlibsView 0KB ");
                return;
            }
            InputStream is = conn.getInputStream();
            if (is == null) {
                MGplayer.MyPrintln("LoadlibsView NULL ");
            }
            String FileStream = sofile + ".so";
            FileOutputStream fos = t.openFileOutput(FileStream, 1);
            byte[] buf = new byte[1024];
            int numreads = 0;
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
                numreads += numread;
                pDialog.setProgress((int) (100.0d * (((double) numreads) / ((double) fileSize))));
            } while (running);
            if (running) {
                String md5 = MGplayer.file_md5(t.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + FileStream);
                MGplayer.MyPrintln("file_md5:" + md5 + " # " + ter_md5);
                if (!(ter_md5 == null || md5 == null || !ter_md5.equals(md5))) {
                    Editor editor = MGplayer.MyGetSharedPreferences(t, "data", 0).edit();
                    editor.putString(sofile + "_version", String.valueOf(version));
                    editor.commit();
                    MGplayer.MyPrintln(sofile + "_version:" + String.valueOf(version));
                    dialog.dismiss();
                    Message msg = new Message();
                    msg.what = 1;
                    rHandler.sendMessage(msg);
                }
            }
            try {
                is.close();
            } catch (Exception e) {
                Log.d("Tag", "error");
            }
            MGplayer.MyPrintln("UpdateActivity Download Finish");
            return;
        }
        Log.d("Tag", "error");
    }
}
