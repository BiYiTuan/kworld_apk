package com.gemini.custom;

public class MyBase64 {
    private static final byte[] decodingTable = new byte[128];
    private static final byte[] encodingTable = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, ClosedCaptionCtrl.END_OF_CAPTION};

    static {
        int i;
        for (i = 0; i < 128; i++) {
            decodingTable[i] = (byte) -1;
        }
        for (i = 65; i <= 90; i++) {
            decodingTable[i] = (byte) (i - 65);
        }
        for (i = 97; i <= 122; i++) {
            decodingTable[i] = (byte) ((i - 97) + 26);
        }
        for (i = 48; i <= 57; i++) {
            decodingTable[i] = (byte) ((i - 48) + 52);
        }
        decodingTable[43] = (byte) 62;
        decodingTable[47] = (byte) 63;
    }

    public static byte[] encode(byte[] data) {
        byte[] bytes;
        int modulus = data.length % 3;
        if (modulus == 0) {
            bytes = new byte[((data.length * 4) / 3)];
        } else {
            bytes = new byte[(((data.length / 3) + 1) * 4)];
        }
        int dataLength = data.length - modulus;
        int i = 0;
        int j = 0;
        while (i < dataLength) {
            int a1 = data[i] & 255;
            int a2 = data[i + 1] & 255;
            int a3 = data[i + 2] & 255;
            bytes[j] = encodingTable[(a1 >>> 2) & 63];
            bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 63];
            bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 63];
            bytes[j + 3] = encodingTable[a3 & 63];
            i += 3;
            j += 4;
        }
        int d1;
        int b2;
        switch (modulus) {
            case 1:
                d1 = data[data.length - 1] & 255;
                b2 = (d1 << 4) & 63;
                bytes[bytes.length - 4] = encodingTable[(d1 >>> 2) & 63];
                bytes[bytes.length - 3] = encodingTable[b2];
                bytes[bytes.length - 2] = (byte) 61;
                bytes[bytes.length - 1] = (byte) 61;
                break;
            case 2:
                d1 = data[data.length - 2] & 255;
                int d2 = data[data.length - 1] & 255;
                b2 = ((d1 << 4) | (d2 >>> 4)) & 63;
                int b3 = (d2 << 2) & 63;
                bytes[bytes.length - 4] = encodingTable[(d1 >>> 2) & 63];
                bytes[bytes.length - 3] = encodingTable[b2];
                bytes[bytes.length - 2] = encodingTable[b3];
                bytes[bytes.length - 1] = (byte) 61;
                break;
        }
        return bytes;
    }

    public static byte[] decode(byte[] data) {
        byte[] bytes;
        data = discardNonBase64Bytes(data);
        if (data[data.length - 2] == (byte) 61) {
            bytes = new byte[((((data.length / 4) - 1) * 3) + 1)];
        } else if (data[data.length - 1] == (byte) 61) {
            bytes = new byte[((((data.length / 4) - 1) * 3) + 2)];
        } else {
            bytes = new byte[((data.length / 4) * 3)];
        }
        int i = 0;
        int j = 0;
        while (i < data.length - 4) {
            byte b1 = decodingTable[data[i]];
            byte b2 = decodingTable[data[i + 1]];
            byte b3 = decodingTable[data[i + 2]];
            byte b4 = decodingTable[data[i + 3]];
            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[j + 2] = (byte) ((b3 << 6) | b4);
            i += 4;
            j += 3;
        }
        if (data[data.length - 2] == (byte) 61) {
            bytes[bytes.length - 1] = (byte) ((decodingTable[data[data.length - 4]] << 2) | (decodingTable[data[data.length - 3]] >> 4));
        } else if (data[data.length - 1] == (byte) 61) {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
        } else {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            b4 = decodingTable[data[data.length - 1]];
            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
        }
        return bytes;
    }

    public static byte[] decode(String data) {
        byte[] bytes;
        data = discardNonBase64Chars(data);
        if (data.charAt(data.length() - 2) == '=') {
            bytes = new byte[((((data.length() / 4) - 1) * 3) + 1)];
        } else if (data.charAt(data.length() - 1) == '=') {
            bytes = new byte[((((data.length() / 4) - 1) * 3) + 2)];
        } else {
            bytes = new byte[((data.length() / 4) * 3)];
        }
        int i = 0;
        int j = 0;
        while (i < data.length() - 4) {
            byte b1 = decodingTable[data.charAt(i)];
            byte b2 = decodingTable[data.charAt(i + 1)];
            byte b3 = decodingTable[data.charAt(i + 2)];
            byte b4 = decodingTable[data.charAt(i + 3)];
            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[j + 2] = (byte) ((b3 << 6) | b4);
            i += 4;
            j += 3;
        }
        if (data.charAt(data.length() - 2) == '=') {
            bytes[bytes.length - 1] = (byte) ((decodingTable[data.charAt(data.length() - 4)] << 2) | (decodingTable[data.charAt(data.length() - 3)] >> 4));
        } else if (data.charAt(data.length() - 1) == '=') {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
        } else {
            b1 = decodingTable[data.charAt(data.length() - 4)];
            b2 = decodingTable[data.charAt(data.length() - 3)];
            b3 = decodingTable[data.charAt(data.length() - 2)];
            b4 = decodingTable[data.charAt(data.length() - 1)];
            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
        }
        return bytes;
    }

    private static byte[] discardNonBase64Bytes(byte[] data) {
        byte[] temp = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            if (isValidBase64Byte(data[i])) {
                int bytesCopied2 = bytesCopied + 1;
                temp[bytesCopied] = data[i];
                bytesCopied = bytesCopied2;
            }
        }
        byte[] newData = new byte[bytesCopied];
        System.arraycopy(temp, 0, newData, 0, bytesCopied);
        return newData;
    }

    private static String discardNonBase64Chars(String data) {
        StringBuffer sb = new StringBuffer();
        int length = data.length();
        for (int i = 0; i < length; i++) {
            if (isValidBase64Byte((byte) data.charAt(i))) {
                sb.append(data.charAt(i));
            }
        }
        return sb.toString();
    }

    private static boolean isValidBase64Byte(byte b) {
        if (b == (byte) 61) {
            return true;
        }
        if (b < (byte) 0 || b >= Byte.MIN_VALUE) {
            return false;
        }
        if (decodingTable[b] == (byte) -1) {
            return false;
        }
        return true;
    }
}
