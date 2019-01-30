package org.videolan.vlc.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.libvlc.util.AndroidUtil;

public class FileUtils {
    public static final String EXTERNAL_PUBLIC_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    private static final int HASH_CHUNK_SIZE = 65536;

    public interface Callback {
        void onResult(boolean z);
    }

    public static String getFileNameFromPath(String path) {
        if (path == null) {
            return "";
        }
        int index = path.lastIndexOf(47);
        if (index > -1) {
            return path.substring(index + 1);
        }
        return path;
    }

    public static String getParent(String path) {
        if (TextUtils.equals(InternalZipConstants.ZIP_FILE_SEPARATOR, path)) {
            return path;
        }
        String parentPath = path;
        if (parentPath.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        }
        int index = parentPath.lastIndexOf(47);
        if (index > 0) {
            parentPath = parentPath.substring(0, index);
        } else if (index == 0) {
            parentPath = InternalZipConstants.ZIP_FILE_SEPARATOR;
        }
        return parentPath;
    }

    public static Uri convertLocalUri(Uri uri) {
        return (TextUtils.equals(uri.getScheme(), "file") && uri.getPath().startsWith("/sdcard")) ? Uri.parse(uri.toString().replace("/sdcard", EXTERNAL_PUBLIC_DIRECTORY)) : uri;
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            return string;
        } finally {
            if (VERSION.SDK_INT >= 16) {
                Util.close(cursor);
            }
        }
    }

    public static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            if (files.length == 0) {
                return false;
            }
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files) {
                if (file.contains(".")) {
                    res &= copyAsset(assetManager, fromAssetPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file, toPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file);
                } else {
                    res &= copyAssetFolder(assetManager, fromAssetPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file, toPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        Exception e;
        Throwable th;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            OutputStream out2 = new FileOutputStream(toPath);
            try {
                copyFile(in, out2);
                out2.flush();
                Util.close(in);
                Util.close(out2);
                out = out2;
                return true;
            } catch (Exception e2) {
                e = e2;
                out = out2;
                try {
                    e.printStackTrace();
                    Util.close(in);
                    Util.close(out);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    Util.close(in);
                    Util.close(out);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                Util.close(in);
                Util.close(out);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            Util.close(in);
            Util.close(out);
            return false;
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public static boolean copyFile(File src, File dst) {
        Throwable th;
        int i = 0;
        boolean ret = true;
        if (src.isDirectory()) {
            File[] filesList = src.listFiles();
            dst.mkdirs();
            int length = filesList.length;
            while (i < length) {
                File file = filesList[i];
                ret &= copyFile(file, new File(dst, file.getName()));
                i++;
            }
        } else if (src.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                OutputStream out2;
                InputStream in2 = new BufferedInputStream(new FileInputStream(src));
                try {
                    out2 = new BufferedOutputStream(new FileOutputStream(dst));
                } catch (FileNotFoundException e) {
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    return false;
                } catch (IOException e2) {
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    throw th;
                }
                try {
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = in2.read(buf);
                        if (len > 0) {
                            out2.write(buf, 0, len);
                        } else {
                            Util.close(in2);
                            Util.close(out2);
                            return true;
                        }
                    }
                } catch (FileNotFoundException e3) {
                    out = out2;
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    return false;
                } catch (IOException e4) {
                    out = out2;
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    out = out2;
                    in = in2;
                    Util.close(in);
                    Util.close(out);
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                Util.close(in);
                Util.close(out);
                return false;
            } catch (IOException e6) {
                Util.close(in);
                Util.close(out);
                return false;
            } catch (Throwable th4) {
                th = th4;
                Util.close(in);
                Util.close(out);
                throw th;
            }
        }
        return ret;
    }

    public static boolean canWrite(String path) {
        boolean z = true;
        if (path == null) {
            return false;
        }
        if (path.startsWith("file://")) {
            path = path.substring(7);
        }
        if (!path.startsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
            return false;
        }
        if (path.startsWith(EXTERNAL_PUBLIC_DIRECTORY)) {
            return true;
        }
        if (AndroidUtil.isKitKatOrLater()) {
            return false;
        }
        File file = new File(path);
        if (!(file.exists() && file.canWrite())) {
            z = false;
        }
        return z;
    }

    public static String computeHash(File file) {
        FileNotFoundException e1;
        Throwable th;
        IOException e;
        long size = file.length();
        long chunkSizeForFile = Math.min(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH, size);
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(file);
            try {
                FileChannel fileChannel = fis2.getChannel();
                long head = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, 0, chunkSizeForFile));
                ByteBuffer bb = ByteBuffer.allocateDirect((int) chunkSizeForFile);
                long position = Math.max(size - PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH, 0);
                while (true) {
                    int read = fileChannel.read(bb, position);
                    if (read > 0) {
                        position += (long) read;
                    } else {
                        bb.flip();
                        Object[] objArr = new Object[1];
                        objArr[0] = Long.valueOf((size + head) + computeHashForChunk(bb));
                        String format = String.format("%016x", objArr);
                        Util.close(fileChannel);
                        Util.close(fis2);
                        fis = fis2;
                        return format;
                    }
                }
            } catch (FileNotFoundException e2) {
                e1 = e2;
                fis = fis2;
                try {
                    e1.printStackTrace();
                    Util.close(null);
                    Util.close(fis);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    Util.close(null);
                    Util.close(fis);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                fis = fis2;
                e.printStackTrace();
                Util.close(null);
                Util.close(fis);
                return null;
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                Util.close(null);
                Util.close(fis);
                throw th;
            }
        } catch (FileNotFoundException e4) {
            e1 = e4;
            e1.printStackTrace();
            Util.close(null);
            Util.close(fis);
            return null;
        } catch (IOException e5) {
            e = e5;
            e.printStackTrace();
            Util.close(null);
            Util.close(fis);
            return null;
        }
    }

    private static long computeHashForChunk(ByteBuffer buffer) {
        LongBuffer longBuffer = buffer.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        long hash = 0;
        while (longBuffer.hasRemaining()) {
            hash += longBuffer.get();
        }
        return hash;
    }
}
