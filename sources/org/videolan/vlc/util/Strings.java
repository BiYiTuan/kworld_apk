package org.videolan.vlc.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import net.lingala.zip4j.util.InternalZipConstants;

public class Strings {
    public static final String TAG = "VLC/UiTools/Strings";

    public static String stripTrailingSlash(String s) {
        if (!s.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) || s.length() <= 1) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    static boolean startsWith(String[] array, String text) {
        for (String item : array) {
            if (text.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    static int containsName(List<String> array, String text) {
        for (int i = array.size() - 1; i >= 0; i--) {
            if (((String) array.get(i)).endsWith(text)) {
                return i;
            }
        }
        return -1;
    }

    public static String millisToString(long millis) {
        return millisToString(millis, false);
    }

    public static String millisToText(long millis) {
        return millisToString(millis, true);
    }

    static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = Math.abs(millis) / 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (text) {
            if (millis > 0) {
                String str;
                StringBuilder stringBuilder = new StringBuilder();
                if (negative) {
                    str = "-";
                } else {
                    str = "";
                }
                return stringBuilder.append(str).append(hours).append("h").append(format.format((long) min)).append("min").toString();
            } else if (min > 0) {
                return (negative ? "-" : "") + min + "min";
            } else {
                return (negative ? "-" : "") + sec + "s";
            }
        } else if (millis > 0) {
            return (negative ? "-" : "") + hours + ":" + format.format((long) min) + ":" + format.format((long) sec);
        } else {
            return (negative ? "-" : "") + min + ":" + format.format((long) sec);
        }
    }

    public static boolean nullEquals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    public static String formatRateString(float rate) {
        return String.format(Locale.US, "%.2fx", new Object[]{Float.valueOf(rate)});
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)) + " " + new String[]{"B", "KiB", "MiB", "GiB", "TiB"}[digitGroups];
    }

    public static String readableSize(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1000.0d));
        return new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1000.0d, (double) digitGroups)) + " " + new String[]{"B", "KB", "MB", "GB", "TB"}[digitGroups];
    }

    public static String removeFileProtocole(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("file://")) {
            return path.substring(7);
        }
        return path;
    }

    public static String buildPkgString(String string) {
        return "org.videolan." + string;
    }
}
