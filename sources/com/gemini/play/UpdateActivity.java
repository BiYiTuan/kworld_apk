package com.gemini.play;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.URLUtil;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyProgressDialog.Builder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.lingala.zip4j.util.InternalZipConstants;
import org.xml.sax.InputSource;

public class UpdateActivity {
    private static String StrURL = "";
    private static String Updatename = "gemini-iptv";
    private static Builder pDialog;
    private static boolean running = true;

    public static void startUpdate(final Activity t, String message) {
        SharedPreferences settings = MGplayer.MyGetSharedPreferences(t, "data", 0);
        running = true;
        String update = settings.getString("update", MGplayer.tv.gete());
        if (update.endsWith(".apk")) {
            StrURL = update;
        } else {
            StrURL = update + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk";
        }
        final MyDialog.Builder builder = new MyDialog.Builder(t);
        builder.setTitle(t.getString(C0216R.string.update_text1));
        if (message == null || message.length() <= 0) {
            builder.setMessage(t.getString(C0216R.string.update_text2).toString());
        } else {
            builder.setMessage(message);
        }
        builder.setPositiveButton(t.getString(C0216R.string.ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UpdateActivity.showUpdate(t, null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(t.getString(C0216R.string.cancel).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void showUpdate(final Activity t, final Handler wHandler) {
        SharedPreferences settings = MGplayer.MyGetSharedPreferences(t, "data", 0);
        running = true;
        String update = settings.getString("update", MGplayer.tv.gete());
        if (update.endsWith(".apk")) {
            StrURL = update;
        } else {
            StrURL = update + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk";
        }
        pDialog = new Builder(t);
        pDialog.setMessage(t.getString(C0216R.string.update_text1).toString()).setNegativeButton(17039360, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UpdateActivity.running = false;
                dialog.dismiss();
                if (wHandler != null) {
                    Message msg = new Message();
                    msg.what = 6;
                    wHandler.sendMessage(msg);
                }
            }
        }).create().show();
        new Thread() {
            public void run() {
                try {
                    UpdateActivity.getDataSource(t, UpdateActivity.StrURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static String readRssXml(InputSource inStream) throws Exception {
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        VesionXMLContent handler = new VesionXMLContent();
        saxParser.parse(inStream, handler);
        return handler.getVersion();
    }

    public String getVersionxml(String resourceUrl) {
        Exception e;
        InputSource inputSource;
        String db = null;
        URL url = null;
        try {
            url = new URL(resourceUrl);
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        try {
            InputSource is = new InputSource(url.openStream());
            if (is != null) {
                try {
                    is.setEncoding("UTF-8");
                    db = readRssXml(is);
                } catch (Exception e3) {
                    e = e3;
                    inputSource = is;
                    e.printStackTrace();
                    return db;
                }
            }
            inputSource = is;
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            return db;
        }
        return db;
    }

    private static void getDataSource(Activity t, String strPath) throws Exception {
        MGplayer.MyPrintln("UpdateActivity Url strPath " + strPath);
        if (URLUtil.isNetworkUrl(strPath)) {
            URLConnection conn = new URL(strPath).openConnection();
            conn.connect();
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                MGplayer.MyPrintln("UpdateActivity APK 0KB ");
                return;
            }
            FileOutputStream fos;
            InputStream is = conn.getInputStream();
            if (is == null) {
                MGplayer.MyPrintln("UpdateActivity APK NULL ");
            }
            MGplayer.MyPrintln("UpdateActivity 1");
            String savePathString = "";
            if (VERSION.SDK_INT >= 24) {
                savePathString = t.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk";
            } else {
                savePathString = t.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk";
            }
            File myTempFile = new File(savePathString);
            MGplayer.MyPrintln("UpdateActivity APK:" + myTempFile.getAbsolutePath() + " url= " + strPath + " version:" + VERSION.SDK_INT);
            String FileStream = Updatename + ".apk";
            if (VERSION.SDK_INT >= 24) {
                fos = t.openFileOutput(FileStream, 0);
            } else {
                fos = t.openFileOutput(FileStream, 1);
            }
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
                if (VERSION.SDK_INT >= 24) {
                    installN(t, FileStream);
                } else {
                    installFile(t, myTempFile);
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

    private static void installFile(Activity t, File f) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.parse("file://" + t.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk"), "application/vnd.android.package-archive");
        t.startActivity(intent);
    }

    public static void installN(Context context, String apkfile) {
        Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(context.getFilesDir(), apkfile));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.addFlags(1);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
