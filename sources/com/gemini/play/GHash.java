package com.gemini.play;

public class GHash {
    static int M_MASK = -2023358767;
    static int M_SHIFT = 0;

    public static int additiveHash(String key, int prime) {
        int hash = key.length();
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
        }
        return hash % prime;
    }

    public static int rotatingHash(String key, int prime) {
        int hash = key.length();
        for (int i = 0; i < key.length(); i++) {
            hash = ((hash << 4) ^ (hash >> 28)) ^ key.charAt(i);
        }
        return hash % prime;
    }

    public static int oneByOneHash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
            hash += hash << 10;
            hash ^= hash >> 6;
        }
        hash += hash << 3;
        hash ^= hash >> 11;
        return hash + (hash << 15);
    }

    public static int bernstein(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * 33) + key.charAt(i);
        }
        return hash;
    }

    public static int universal(char[] key, int mask, int[] tab) {
        int hash = key.length;
        int len = key.length;
        for (int i = 0; i < (len << 3); i += 8) {
            char k = key[i >> 3];
            if ((k & 1) == 0) {
                hash ^= tab[i + 0];
            }
            if ((k & 2) == 0) {
                hash ^= tab[i + 1];
            }
            if ((k & 4) == 0) {
                hash ^= tab[i + 2];
            }
            if ((k & 8) == 0) {
                hash ^= tab[i + 3];
            }
            if ((k & 16) == 0) {
                hash ^= tab[i + 4];
            }
            if ((k & 32) == 0) {
                hash ^= tab[i + 5];
            }
            if ((k & 64) == 0) {
                hash ^= tab[i + 6];
            }
            if ((k & 128) == 0) {
                hash ^= tab[i + 7];
            }
        }
        return hash & mask;
    }

    public static int zobrist(char[] key, int mask, int[][] tab) {
        int hash = key.length;
        for (int i = 0; i < key.length; i++) {
            hash ^= tab[i][key[i]];
        }
        return hash & mask;
    }

    public static int FNVHash(byte[] data) {
        int hash = -2128831035;
        for (byte b : data) {
            hash = (16777619 * hash) ^ b;
        }
        return M_SHIFT == 0 ? hash : ((hash >> M_SHIFT) ^ hash) & M_MASK;
    }

    public static int FNVHash1(byte[] data) {
        int hash = -2128831035;
        for (byte b : data) {
            hash = (hash ^ b) * 16777619;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        return hash + (hash << 5);
    }

    public static int FNVHash1(String data) {
        int hash = -2128831035;
        for (int i = 0; i < data.length(); i++) {
            hash = (data.charAt(i) ^ hash) * 16777619;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        return hash + (hash << 5);
    }

    public static int intHash(int key) {
        key += (key << 15) ^ -1;
        key ^= key >>> 10;
        key += key << 3;
        key ^= key >>> 6;
        key += (key << 11) ^ -1;
        return key ^ (key >>> 16);
    }

    public static int RSHash(String str) {
        int a = 63689;
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * a) + str.charAt(i);
            a *= 378551;
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int JSHash(String str) {
        int hash = 1315423911;
        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i)) + (hash >> 2);
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int PJWHash(String str) {
        int ThreeQuarters = 96 / 4;
        int OneEighth = 32 / 8;
        int HighBits = -1 << 28;
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);
            int test = hash & HighBits;
            if (test != 0) {
                hash = ((test >> 24) ^ hash) & 268435455;
            }
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int ELFHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);
            int x = (int) (((long) hash) & 4026531840L);
            if (x != 0) {
                hash = (hash ^ (x >> 24)) & (x ^ -1);
            }
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int BKDRHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * 131) + str.charAt(i);
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int SDBMHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = ((str.charAt(i) + (hash << 6)) + (hash << 16)) - hash;
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int DJBHash(String str) {
        int hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int DEKHash(String str) {
        int hash = str.length();
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }
        return Integer.MAX_VALUE & hash;
    }

    public static int APHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            int charAt;
            if ((i & 1) == 0) {
                charAt = ((hash << 7) ^ str.charAt(i)) ^ (hash >> 3);
            } else {
                charAt = (((hash << 11) ^ str.charAt(i)) ^ (hash >> 5)) ^ -1;
            }
            hash ^= charAt;
        }
        return hash;
    }

    public static int java(String str) {
        int h = 0;
        int i = 0;
        int off = 0;
        while (i < str.length()) {
            h = (h * 31) + str.charAt(off);
            i++;
            off++;
        }
        return h;
    }

    public static long mixHash(String str) {
        return (((long) str.hashCode()) << 32) | ((long) FNVHash1(str));
    }
}
