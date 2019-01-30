package io.vov.vitamio.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private Cipher ecipher;

    public Crypto(String key) {
        try {
            setupCrypto(new SecretKeySpec(generateKey(key), "AES"));
        } catch (Throwable e) {
            Log.m32e("Crypto", e);
        }
    }

    private void setupCrypto(SecretKey key) {
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15});
        try {
            this.ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.ecipher.init(1, key, paramSpec);
        } catch (Throwable e) {
            this.ecipher = null;
            Log.m32e("setupCrypto", e);
        }
    }

    public String encrypt(String plaintext) {
        if (this.ecipher == null) {
            return "";
        }
        try {
            return Base64.encodeToString(this.ecipher.doFinal(plaintext.getBytes("UTF-8")), 2);
        } catch (Throwable e) {
            Log.m32e("encryp", e);
            return "";
        }
    }

    public static String md5(String plain) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(plain.getBytes());
            String bigInteger = new BigInteger(1, m.digest()).toString(16);
            while (bigInteger.length() < 32) {
                bigInteger = "0" + bigInteger;
            }
            return bigInteger;
        } catch (Exception e) {
            return "";
        }
    }

    private static byte[] generateKey(String input) {
        try {
            return MessageDigest.getInstance("SHA256").digest(input.getBytes("UTF-8"));
        } catch (Throwable e) {
            Log.m32e("generateKey", e);
            return null;
        }
    }

    private PublicKey readKeyFromStream(InputStream keyStream) throws IOException {
        PublicKey pubKey;
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(keyStream));
        try {
            pubKey = (PublicKey) oin.readObject();
        } catch (Throwable e) {
            Log.m32e("readKeyFromStream", e);
            pubKey = null;
        } finally {
            oin.close();
        }
        return pubKey;
    }

    public String rsaEncrypt(InputStream keyStream, String data) {
        try {
            return rsaEncrypt(keyStream, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String rsaEncrypt(InputStream keyStream, byte[] data) {
        try {
            PublicKey pubKey = readKeyFromStream(keyStream);
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(1, pubKey);
            return Base64.encodeToString(cipher.doFinal(data), 2);
        } catch (Throwable e) {
            Log.m32e("rsaEncrypt", e);
            return "";
        }
    }
}
