package com.gemini.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import com.gemini.play.LIVEplayer;
import com.gemini.play.MGplayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class quanxing {
    public static Runnable checkRunRunnable;
    public static int checkruntimes = 0;
    public static String checkrunurl = null;
    public static int checktimeout = 0;
    public static String exit_url = null;
    public static String hotlink = null;
    public static String iconpassword = null;
    public static String iconurl = null;
    public static int iconversion = 0;
    public static int load_timeout = 0;
    public static String login_url = "http://174.127.103.148:9000/exit.asp";
    public static int panel = 0;
    public static String path = "/data/data/org.validate.steven/temp/";
    public static String qxid = "qxid.dat";
    public static String tip_text8 = null;
    public static ArrayList<String> tips = new ArrayList();
    public static String urlpassword = null;
    public static int version = 24;
    public static String xmlpassword = null;

    public static void init(Context _this) {
        runCheck();
    }

    public static void previewInit() {
        String xml_path = "/data/data/" + MGplayer.tv.getPackName() + "/temp/xml/";
        for (int ii = 0; ii < LIVEplayer.urlSize(); ii++) {
            String xml_file = LIVEplayer.introidGet(ii);
            MGplayer.MyPrintln("preview xml " + ii + " file " + xml_file);
            if (!xml_file.equals("x.xml")) {
                List<PreviewInfo> infos = getResult(xml_path + xml_file);
                String temp = "";
                if (infos != null) {
                    for (PreviewInfo info : infos) {
                        temp = temp + info.getTime() + "#" + info.getName() + "|";
                    }
                    MGplayer.MyPrintln("preview init temp:" + temp);
                    LIVEplayer.introductionSet(ii, temp);
                }
            }
        }
    }

    public static void runCheck() {
        final Handler checkRunHandler = new Handler();
        checkRunRunnable = new Runnable() {
            public void run() {
                if (!(LIVEplayer.currentURL == null || quanxing.checkrunurl == null)) {
                    String[] arrs = LIVEplayer.currentURL.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    if (arrs.length < 4) {
                        checkRunHandler.postDelayed(quanxing.checkRunRunnable, (long) (quanxing.checkruntimes * 1000));
                        return;
                    }
                    String url;
                    String p2pId = arrs[2];
                    String info = "";
                    try {
                        info = URLEncoder.encode(MGplayer.sendHttpRequestpercent(p2pId, MGplayer.port()), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (quanxing.checkrunurl.indexOf("?") != 0) {
                        url = quanxing.checkrunurl + "?chid=" + p2pId + "&info=" + info;
                    } else {
                        url = quanxing.checkrunurl + "&chid=" + p2pId + "&info=" + info;
                    }
                    MGplayer.MyPrintln("check run = " + url);
                    String data = MGplayer.sendServerCmd(url, quanxing.checktimeout);
                    MGplayer.MyPrintln("====== checkRunRunnable data =======" + data);
                    if (data.equals("finish")) {
                        System.exit(1);
                    }
                }
                checkRunHandler.postDelayed(quanxing.checkRunRunnable, (long) (quanxing.checkruntimes * 1000));
            }
        };
        checkRunHandler.post(checkRunRunnable);
    }

    public static void unXml(Context _this) {
        path = "/data/data/" + MGplayer.tv.getPackName() + "/temp/";
        SharedPreferences settings = MGplayer.MyGetSharedPreferences(_this, "data", 0);
        String stime = settings.getString("epgtime", "1970-1-1");
        if (DateCompare(stime, settings.getString("epgoldtime", "1970-1-1"))) {
            String url = settings.getString("epgurl", MGplayer.tv.gete() + "/xml.zip");
            System.out.println("xml path: " + url);
            String name = url.substring(url.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1);
            System.out.println("xml name: " + name);
            try {
                donwFile(url, path, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (new File(path + InternalZipConstants.ZIP_FILE_SEPARATOR + name).exists()) {
                Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
                editor.putString("epgoldtime", stime);
                editor.commit();
                unzipFile(path + name, path, xmlpassword);
            }
        }
    }

    public static void unIcon(Context _this) {
        String iconurl_save;
        path = "/data/data/" + MGplayer.tv.getPackName() + "/temp/";
        int iconversion_save = MGplayer.MyGetSharedPreferences(_this, "data", 0).getInt("iconversion", 0);
        MGplayer.MyPrintln("iconversion save:" + iconversion_save + " iconversion:" + iconversion);
        if (iconurl == null) {
            iconurl_save = "http://android.webok.net:9000/epg/icon.zip";
        } else {
            iconurl_save = iconurl;
        }
        System.out.println("iconurl path: " + iconurl);
        String iconname = iconurl.substring(iconurl_save.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1);
        if (iconversion_save < iconversion || !MGplayer.fileIsExists(path + InternalZipConstants.ZIP_FILE_SEPARATOR + iconname)) {
            System.out.println("iconurl name: " + iconname);
            try {
                donwFile(iconurl_save, path, iconname);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (new File(path + InternalZipConstants.ZIP_FILE_SEPARATOR + iconname).exists()) {
                unzipFile(path + iconname, path, iconpassword);
                Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
                editor.putInt("iconversion", iconversion);
                editor.commit();
            }
        }
    }

    public static void unzipFile(String infile, String outdir, String passwd) {
        ZipFile zipFile;
        ZipException e;
        try {
            ZipFile zfile = new ZipFile(new File(infile));
            try {
                zfile.setFileNameCharset("gbk");
                if (zfile.isValidZipFile()) {
                    File odir = new File(outdir);
                    if (odir.isDirectory() && !odir.exists()) {
                        odir.mkdir();
                    }
                    if (zfile.isEncrypted()) {
                        zfile.setPassword(passwd.toCharArray());
                    }
                    zfile.extractAll(outdir);
                    zipFile = zfile;
                    return;
                }
                zipFile = zfile;
            } catch (ZipException e2) {
                e = e2;
                zipFile = zfile;
                e.printStackTrace();
            }
        } catch (ZipException e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    public static void donwFile(String str, String outdir, String filename) throws IOException {
        File file = new File(outdir + filename);
        new File(outdir).mkdir();
        file.createNewFile();
        String fileName = filename;
        String path = outdir;
        MGplayer.MyPrintln("path:" + path + "#fileName:" + fileName);
        URLConnection conn = new URL(str).openConnection();
        conn.connect();
        if (conn.getContentLength() > 0) {
            InputStream is = conn.getInputStream();
            if (is == null) {
                MGplayer.MyPrintln("WGET DOWN NULL");
            }
            MGplayer.MyPrintln("WGET 1");
            FileOutputStream fos = new FileOutputStream(new File(path + fileName));
            byte[] buf = new byte[1024];
            while (true) {
                int numread = is.read(buf);
                if (numread > 0) {
                    fos.write(buf, 0, numread);
                } else {
                    return;
                }
            }
        }
    }

    public static void saveToSDCard(String name, String content) {
        Exception e;
        Throwable th;
        if (MGplayer.fileIsExists(Environment.getExternalStorageDirectory().toString())) {
            FileOutputStream fos = null;
            try {
                long size1 = getSDTotalSize();
                long size2 = getSDAvailableSize();
                System.out.println("size1 =" + size1);
                System.out.println("size1 =" + size2);
                if (getSDTotalSize() > 10 && getSDAvailableSize() > 10) {
                    FileOutputStream fos2 = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), name));
                    try {
                        fos2.write(content.getBytes());
                        fos = fos2;
                    } catch (Exception e2) {
                        e = e2;
                        fos = fos2;
                        try {
                            e.printStackTrace();
                            try {
                                fos.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                return;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                fos.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fos = fos2;
                        fos.close();
                        throw th;
                    }
                }
                try {
                    fos.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                fos.close();
            }
        }
    }

    private static long getSDTotalSize() {
        if (!MGplayer.fileIsExists(Environment.getExternalStorageDirectory().toString())) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) stat.getBlockSize()) * ((long) stat.getBlockCount())) / 1048576;
    }

    private static long getSDAvailableSize() {
        if (!MGplayer.fileIsExists(Environment.getExternalStorageDirectory().toString())) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) stat.getBlockSize()) * ((long) stat.getAvailableBlocks())) / 1048576;
    }

    public static String readSDFile(String fileName) {
        if (!MGplayer.fileIsExists(Environment.getExternalStorageDirectory().toString()) || !MGplayer.fileIsExists(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + fileName)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(new File(Environment.getExternalStorageDirectory(), fileName));
            while (true) {
                int c = fis.read();
                if (c == -1) {
                    break;
                }
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean DateCompare(String s1, String s2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(s1);
            d2 = sdf.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d1.getTime() - d2.getTime() > 0) {
            return true;
        }
        return false;
    }

    public static List<PreviewInfo> parseXmlfile(String xmlfile) {
        XmlHandler handler = new XmlHandler();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            if (xmlfile.length() <= 1 || !xmlfile.endsWith(".xml") || !new File(xmlfile).exists()) {
                return null;
            }
            System.out.println("====== xmlfile =======" + xmlfile);
            InputStream input = new FileInputStream(xmlfile);
            parser.parse(new InputSource(input), handler);
            input.close();
            return handler.getInfos();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static List<PreviewInfo> getResult(String file) {
        List<PreviewInfo> infos = parseXmlfile(file);
        if (infos == null) {
            return null;
        }
        List<PreviewInfo> ins = new ArrayList();
        if (ins == null) {
            return null;
        }
        for (PreviewInfo info : infos) {
            int num = Calendar.getInstance().get(7) - 1;
            if (num == 0) {
                num = 7;
            }
            if (("Week" + num).equals(info.getWeek())) {
                ins.add(info);
            }
        }
        return ins;
    }

    public static void donloadBackground(Context _this, String image) {
        MGplayer.MyPrintln("donloadBackground:" + image);
        if (image.equals("null")) {
            Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
            editor.remove("loadimage");
            editor.commit();
            return;
        }
        Thread_LoadImage(_this, image);
    }

    public static void Thread_LoadImage(final Context _this, final String image) {
        new Thread() {
            public void run() {
                String imagePath = image;
                if (imagePath != null) {
                    String[] images = image.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    if (images.length >= 3) {
                        String imageName = images[images.length - 1];
                        String saveImageName = _this.getFilesDir() + "/background/" + imageName;
                        MGplayer.MyPrintln("Loadingground:" + imageName + " path:" + saveImageName);
                        if (!MGplayer.fileIsExists(saveImageName)) {
                            try {
                                MGplayer.donwFile(imagePath, saveImageName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (MGplayer.fileIsExists(saveImageName)) {
                            Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
                            editor.putString("loadimage", imageName);
                            editor.commit();
                        }
                    }
                }
            }
        }.start();
    }
}
