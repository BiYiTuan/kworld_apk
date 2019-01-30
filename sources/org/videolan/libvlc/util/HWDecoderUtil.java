package org.videolan.libvlc.util;

import java.util.HashMap;

public class HWDecoderUtil {
    public static final boolean HAS_SUBTITLES_SURFACE = AndroidUtil.isGingerbreadOrLater();
    private static final AudioOutputBySOC[] sAudioOutputBySOCList = new AudioOutputBySOC[]{new AudioOutputBySOC("ro.product.brand", "Amazon", AudioOutput.OPENSLES), new AudioOutputBySOC("ro.product.manufacturer", "Amazon", AudioOutput.OPENSLES)};
    private static final DecoderBySOC[] sBlacklistedDecoderBySOCList = new DecoderBySOC[]{new DecoderBySOC("ro.product.board", "MSM8225", Decoder.NONE), new DecoderBySOC("ro.product.board", "hawaii", Decoder.NONE)};
    private static final DecoderBySOC[] sDecoderBySOCList = new DecoderBySOC[]{new DecoderBySOC("ro.product.brand", "SEMC", Decoder.NONE), new DecoderBySOC("ro.board.platform", "msm7627", Decoder.NONE), new DecoderBySOC("ro.product.brand", "Amazon", Decoder.MEDIACODEC), new DecoderBySOC("ro.board.platform", "omap3", Decoder.OMX), new DecoderBySOC("ro.board.platform", "rockchip", Decoder.OMX), new DecoderBySOC("ro.board.platform", "rk29", Decoder.OMX), new DecoderBySOC("ro.board.platform", "msm7630", Decoder.OMX), new DecoderBySOC("ro.board.platform", "s5pc", Decoder.OMX), new DecoderBySOC("ro.board.platform", "montblanc", Decoder.OMX), new DecoderBySOC("ro.board.platform", "exdroid", Decoder.OMX), new DecoderBySOC("ro.board.platform", "sun6i", Decoder.OMX), new DecoderBySOC("ro.board.platform", "exynos4", Decoder.MEDIACODEC), new DecoderBySOC("ro.board.platform", "omap4", Decoder.ALL), new DecoderBySOC("ro.board.platform", "tegra", Decoder.ALL), new DecoderBySOC("ro.board.platform", "tegra3", Decoder.ALL), new DecoderBySOC("ro.board.platform", "msm8660", Decoder.ALL), new DecoderBySOC("ro.board.platform", "exynos5", Decoder.ALL), new DecoderBySOC("ro.board.platform", "rk30", Decoder.ALL), new DecoderBySOC("ro.board.platform", "rk31", Decoder.ALL), new DecoderBySOC("ro.board.platform", "mv88de3100", Decoder.ALL), new DecoderBySOC("ro.hardware", "mt83", Decoder.ALL)};
    private static final HashMap<String, String> sSystemPropertyMap = new HashMap();

    public enum AudioOutput {
        OPENSLES,
        AUDIOTRACK,
        ALL
    }

    private static class AudioOutputBySOC {
        public final AudioOutput aout;
        public final String key;
        public final String value;

        public AudioOutputBySOC(String key, String value, AudioOutput aout) {
            this.key = key;
            this.value = value;
            this.aout = aout;
        }
    }

    public enum Decoder {
        UNKNOWN,
        NONE,
        OMX,
        MEDIACODEC,
        ALL
    }

    private static class DecoderBySOC {
        public final Decoder dec;
        public final String key;
        public final String value;

        public DecoderBySOC(String key, String value, Decoder dec) {
            this.key = key;
            this.value = value;
            this.dec = dec;
        }
    }

    public static Decoder getDecoderFromDevice() {
        int i = 0;
        for (DecoderBySOC decBySOC : sBlacklistedDecoderBySOCList) {
            DecoderBySOC decBySOC2;
            String prop = getSystemPropertyCached(decBySOC2.key);
            if (prop != null && prop.contains(decBySOC2.value)) {
                return decBySOC2.dec;
            }
        }
        if (AndroidUtil.isJellyBeanMR2OrLater()) {
            return Decoder.ALL;
        }
        if (AndroidUtil.isHoneycombOrLater()) {
            DecoderBySOC[] decoderBySOCArr = sDecoderBySOCList;
            int length = decoderBySOCArr.length;
            while (i < length) {
                decBySOC2 = decoderBySOCArr[i];
                prop = getSystemPropertyCached(decBySOC2.key);
                if (prop != null && prop.contains(decBySOC2.value)) {
                    return decBySOC2.dec;
                }
                i++;
            }
        }
        return Decoder.UNKNOWN;
    }

    public static AudioOutput getAudioOutputFromDevice() {
        if (!AndroidUtil.isGingerbreadOrLater()) {
            return AudioOutput.AUDIOTRACK;
        }
        for (AudioOutputBySOC aoutBySOC : sAudioOutputBySOCList) {
            String prop = getSystemPropertyCached(aoutBySOC.key);
            if (prop != null && prop.contains(aoutBySOC.value)) {
                return aoutBySOC.aout;
            }
        }
        return AudioOutput.ALL;
    }

    private static String getSystemPropertyCached(String key) {
        String prop = (String) sSystemPropertyMap.get(key);
        if (prop != null) {
            return prop;
        }
        prop = getSystemProperty(key, "none");
        sSystemPropertyMap.put(key, prop);
        return prop;
    }

    private static String getSystemProperty(String key, String def) {
        try {
            Class<?> SystemProperties = ClassLoader.getSystemClassLoader().loadClass("android.os.SystemProperties");
            return (String) SystemProperties.getMethod("get", new Class[]{String.class, String.class}).invoke(SystemProperties, new Object[]{key, def});
        } catch (Exception e) {
            return def;
        }
    }
}
