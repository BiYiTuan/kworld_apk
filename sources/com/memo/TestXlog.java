package com.memo;

import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;

public class TestXlog {
    public static boolean isTest = true;

    /* renamed from: d */
    public static void m27d(String str) {
        if (!TextUtils.isEmpty(str)) {
            reflect("i", str);
        }
    }

    public static void d2(String str) {
        reflect2("d", str);
    }

    /* renamed from: e */
    public static void m28e(String str) {
        if (!TextUtils.isEmpty(str)) {
            reflect("e", str);
        }
    }

    /* renamed from: i */
    public static void m29i(String str) {
        if (!TextUtils.isEmpty(str)) {
            reflect("i", str);
        }
    }

    public static void i2(String str) {
        reflect2("i", str);
    }

    private static void reflect(String str, String str2) {
        try {
            Class cls = Class.forName("com.elvishew.xlog.XlogInstance");
            if (cls != null) {
                cls.getMethod(str, new Class[]{String.class}).invoke(cls, new Object[]{str2});
            }
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        } catch (IllegalAccessException e3) {
        } catch (InvocationTargetException e4) {
        }
    }

    private static void reflect2(String str, String str2) {
        try {
            Class cls = Class.forName("com.elvishew.xlog.XlogInstance2");
            if (cls != null) {
                cls.getMethod(str, new Class[]{String.class}).invoke(cls, new Object[]{str2});
            }
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        } catch (IllegalAccessException e3) {
        } catch (InvocationTargetException e4) {
        }
    }

    /* renamed from: v */
    public static void m30v(String str) {
        if (!TextUtils.isEmpty(str)) {
            reflect("v", str);
        }
    }
}
