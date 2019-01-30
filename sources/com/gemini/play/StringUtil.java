package com.gemini.play;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {
    private static NumberFormat nf = new DecimalFormat("#,##0.00");

    public static String N2S(Number num) {
        if (num != null) {
            return nf.format(num);
        }
        return null;
    }

    public static boolean isEmpty(String obj) {
        return obj == null || "".equals(obj.trim());
    }

    public static boolean isEmpty(Date obj) {
        return obj == null;
    }

    public static boolean isEmpty(Long obj) {
        return isEmpty(obj, false);
    }

    public static boolean isEmpty(Long obj, boolean flag) {
        if (obj == null) {
            return true;
        }
        if (flag && 0 == obj.longValue()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Integer obj) {
        return isEmpty(obj, false);
    }

    public static boolean isEmpty(Integer obj, boolean flag) {
        if (obj == null) {
            return true;
        }
        if (flag && obj.intValue() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Double obj) {
        return isEmpty(obj, false);
    }

    public static boolean isEmpty(Double obj, boolean flag) {
        if (obj == null) {
            return true;
        }
        if (flag && 0.0d == obj.doubleValue()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static Double S2D(String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        try {
            return new Double(nf.parse(value).doubleValue());
        } catch (ParseException pe) {
            throw new RuntimeException("", pe);
        }
    }

    public static String doubleFormat(Double value) {
        if (value == null) {
            value = new Double(0.0d);
        }
        return nf.format(value);
    }

    private static Long L2S(String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        return Long.valueOf(value);
    }

    private static Integer I2S(String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        if (value.trim().equalsIgnoreCase("false")) {
            return new Integer(0);
        }
        if (value.trim().equalsIgnoreCase("true")) {
            return new Integer(1);
        }
        return Integer.valueOf(value);
    }

    public static Long S2L(String value) {
        return L2S(parseComStr2SmpStr(value));
    }

    public static Integer S2I(String value) {
        return I2S(parseComStr2SmpStr(value));
    }

    public static BigDecimal S2B(String value) {
        String s = parseComStr2SmpStr(value);
        if (s == null || "".equals(s)) {
            return null;
        }
        return new BigDecimal(s);
    }

    public static Double parseString2Double(String value) {
        return S2D(parseComStr2SmpStr(value));
    }

    public static String parseComStr2SmpStr(String value) {
        if (value == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(value, ",");
        String str = "";
        while (st.hasMoreTokens()) {
            str = str + st.nextToken();
        }
        return str;
    }

    public static boolean isStringNotEmpty(String s) {
        return (s == null || "".equals(s.trim())) ? false : true;
    }

    public static boolean isStringEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static String format(String src, Object[] objects) {
        int k = 0;
        for (Object obj : objects) {
            src = src.replace("{" + k + "}", obj.toString());
            k++;
        }
        return src;
    }

    public static String getAfterColon(String s) {
        if (s == null) {
            return null;
        }
        String str1 = s.substring(s.indexOf(":") + 1);
        int index1 = str1.indexOf("[");
        return str1.substring(index1 + 1, str1.indexOf("]"));
    }
}
