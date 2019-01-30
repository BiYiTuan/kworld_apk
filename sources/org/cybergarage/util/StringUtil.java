package org.cybergarage.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;

public final class StringUtil {
    private static char[] key;

    static {
        if (TextUtils.equals("tubicast", "azcast")) {
            key = new char[]{'Ý', 'Þ', 'ß', 'à', 'â', 'â'};
        } else {
            key = new char[]{'Þ', 'Ý', 'ß', 'á', 'á', 'â'};
        }
    }

    public static String eds(String str) {
        int i = 0;
        Log.d("gggl", "111111111   tubicast");
        byte[] bArr = new byte[0];
        try {
            bArr = str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        while (i < bArr.length) {
            bArr[i] = (byte) ((((byte) (key[i % key.length] & 255)) ^ bArr[i]) & 255);
            i++;
        }
        try {
            return new String(bArr, "ISO-8859-1");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static byte[] eds(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) ((((byte) (key[i % key.length] & 255)) ^ bArr[i]) & 255);
        }
        return bArr;
    }

    public static byte[] edsReturnBuffer(String str) {
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((((byte) (key[i % key.length] & 255)) ^ bytes[i]) & 255);
        }
        return bytes;
    }

    public static byte[] edsReturnBuffer(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) ((((byte) (key[i % key.length] & 255)) ^ bArr[i]) & 255);
        }
        return bArr;
    }

    public static final int findFirstNotOf(String str, String str2) {
        return findOf(str, str2, 0, str.length() - 1, 1, false);
    }

    public static final int findFirstOf(String str, String str2) {
        return findOf(str, str2, 0, str.length() - 1, 1, true);
    }

    public static final int findLastNotOf(String str, String str2) {
        return findOf(str, str2, str.length() - 1, 0, -1, false);
    }

    public static final int findLastOf(String str, String str2) {
        return findOf(str, str2, str.length() - 1, 0, -1, true);
    }

    public static final int findOf(String str, String str2, int i, int i2, int i3, boolean z) {
        if (i3 == 0) {
            return -1;
        }
        int length = str2.length();
        int i4 = i;
        while (true) {
            char charAt;
            int i5;
            int i6;
            char charAt2;
            if (i3 > 0) {
                if (i2 < i4) {
                    break;
                }
                charAt = str.charAt(i4);
                i5 = 0;
                for (i6 = 0; i6 < length; i6++) {
                    charAt2 = str2.charAt(i6);
                    if (z) {
                        if (charAt != charAt2) {
                            i5++;
                        }
                        if (i5 == length) {
                            return i4;
                        }
                    } else if (charAt == charAt2) {
                        return i4;
                    }
                }
                i4 += i3;
            } else {
                if (i4 < i2) {
                    break;
                }
                charAt = str.charAt(i4);
                i5 = 0;
                for (i6 = 0; i6 < length; i6++) {
                    charAt2 = str2.charAt(i6);
                    if (z) {
                        if (charAt != charAt2) {
                            i5++;
                        }
                        if (i5 == length) {
                            return i4;
                        }
                    } else if (charAt == charAt2) {
                        return i4;
                    }
                }
                i4 += i3;
            }
        }
        return -1;
    }

    public static final boolean hasData(String str) {
        return str != null && str.length() > 0;
    }

    public static final int toInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            Debug.warning(e);
            return 0;
        }
    }

    public static final long toLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            Debug.warning(e);
            return 0;
        }
    }

    public static final String trim(String str, String str2) {
        int findFirstNotOf = findFirstNotOf(str, str2);
        if (findFirstNotOf < 0) {
            return str;
        }
        str = str.substring(findFirstNotOf, str.length());
        findFirstNotOf = findLastNotOf(str, str2);
        return findFirstNotOf >= 0 ? str.substring(0, findFirstNotOf + 1) : str;
    }
}
