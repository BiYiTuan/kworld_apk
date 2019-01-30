package org.cybergarage.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtil {
    public static final boolean isXMLFileName(String str) {
        return !StringUtil.hasData(str) ? false : str.toLowerCase().endsWith("xml");
    }

    public static final byte[] load(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (Exception e) {
            Debug.warning(e);
            return new byte[0];
        }
    }

    public static final byte[] load(FileInputStream fileInputStream) {
        byte[] bArr = new byte[524288];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int read = fileInputStream.read(bArr);
            while (read > 0) {
                byteArrayOutputStream.write(bArr, 0, read);
                read = fileInputStream.read(bArr);
            }
            fileInputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Debug.warning(e);
            return new byte[0];
        }
    }

    public static final byte[] load(String str) {
        try {
            return load(new FileInputStream(str));
        } catch (Exception e) {
            Debug.warning(e);
            return new byte[0];
        }
    }

    public static void writeFile(String str, String str2) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str2, true);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
