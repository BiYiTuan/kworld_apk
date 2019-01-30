package org.videolan.vlc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.util.Log;
import java.util.ArrayList;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer.Equalizer;
import org.videolan.libvlc.util.HWDecoderUtil;
import org.videolan.libvlc.util.HWDecoderUtil.AudioOutput;
import org.videolan.libvlc.util.VLCUtil;
import org.videolan.libvlc.util.VLCUtil.MachineSpecs;

public class VLCOptions {
    public static final int AOUT_AUDIOTRACK = 0;
    public static final int AOUT_OPENSLES = 1;
    public static final int HW_ACCELERATION_AUTOMATIC = -1;
    public static final int HW_ACCELERATION_DECODING = 0;
    public static final int HW_ACCELERATION_DISABLED = 1;
    public static final int HW_ACCELERATION_FULL = 2;
    public static final int MEDIA_FORCE_AUDIO = 8;
    public static final int MEDIA_NO_HWACCEL = 2;
    public static final int MEDIA_PAUSED = 4;
    public static final int MEDIA_VIDEO = 1;
    private static final String TAG = "VLCConfig";

    public static ArrayList<String> getLibOptions(Context context) {
        boolean timeStrechingDefault;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> options = new ArrayList(50);
        if (VERSION.SDK_INT >= 19) {
            timeStrechingDefault = true;
        } else {
            timeStrechingDefault = false;
        }
        boolean timeStreching = pref.getBoolean("enable_time_stretching_audio", timeStrechingDefault);
        String subtitlesEncoding = pref.getString("subtitle_text_encoding", "");
        boolean frameSkip = pref.getBoolean("enable_frame_skip", false);
        if (pref.getString("chroma_format", "RV16").equals("YV12")) {
            String chroma = "";
        }
        boolean verboseMode = pref.getBoolean("enable_verbose_mode", true);
        int deblocking = -1;
        try {
            deblocking = getDeblocking(Integer.parseInt(pref.getString("deblocking", "-1")));
        } catch (NumberFormatException e) {
        }
        int networkCaching = pref.getInt("network_caching_value", 0);
        if (networkCaching > 1000) {
            networkCaching = 1000;
        } else if (networkCaching < 0) {
            networkCaching = 1000;
        }
        int opengl = Integer.parseInt(pref.getString("opengl", "-1"));
        options.add(timeStreching ? "--audio-time-stretch" : "--no-audio-time-stretch");
        options.add("--avcodec-skiploopfilter");
        options.add("" + deblocking);
        options.add("--avcodec-skip-frame");
        options.add(frameSkip ? "2" : "0");
        options.add("--avcodec-skip-idct");
        options.add(frameSkip ? "2" : "0");
        options.add("--no-stats");
        if (networkCaching > 0) {
            options.add("--network-caching=" + networkCaching);
        }
        options.add("--audio-resampler");
        options.add(getResampler());
        if (opengl == 1) {
            options.add("--vout=gles2,none");
        } else if (opengl == 0) {
            options.add("--vout=android_display,none");
        }
        options.add(verboseMode ? "-vv" : "-v");
        return options;
    }

    public static String getAout(SharedPreferences pref) {
        int aout = -1;
        try {
            aout = Integer.parseInt(pref.getString("aout", "-1"));
        } catch (NumberFormatException e) {
        }
        AudioOutput hwaout = HWDecoderUtil.getAudioOutputFromDevice();
        if (hwaout == AudioOutput.AUDIOTRACK || hwaout == AudioOutput.OPENSLES) {
            aout = hwaout == AudioOutput.OPENSLES ? 1 : 0;
        }
        return aout == 1 ? "opensles_android" : "android_audiotrack";
    }

    private static int getDeblocking(int deblocking) {
        int ret = deblocking;
        if (deblocking < 0) {
            MachineSpecs m = VLCUtil.getMachineSpecs();
            if (m == null) {
                return ret;
            }
            if ((m.hasArmV6 && !m.hasArmV7) || m.hasMips) {
                ret = 4;
            } else if (m.frequency >= 1200.0f && m.processors > 2) {
                ret = 1;
            } else if (m.bogoMIPS < 1200.0f || m.processors <= 2) {
                ret = 3;
            } else {
                ret = 1;
                Log.d(TAG, "Used bogoMIPS due to lack of frequency info");
            }
        } else if (deblocking > 4) {
            ret = 3;
        }
        return ret;
    }

    private static String getResampler() {
        MachineSpecs m = VLCUtil.getMachineSpecs();
        return (m == null || m.processors > 2) ? "soxr" : "ugly";
    }

    public static void setMediaOptions(Media media, Context context, int flags) {
        boolean noHardwareAcceleration;
        boolean paused;
        if ((flags & 2) != 0) {
            noHardwareAcceleration = true;
        } else {
            noHardwareAcceleration = false;
        }
        boolean noVideo;
        if ((flags & 1) == 0) {
            noVideo = true;
        } else {
            noVideo = false;
        }
        if ((flags & 4) != 0) {
            paused = true;
        } else {
            paused = false;
        }
        int hardwareAcceleration = 1;
        if (!noHardwareAcceleration) {
            try {
                hardwareAcceleration = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("hardware_acceleration", "-1"));
            } catch (NumberFormatException e) {
            }
        }
        if (hardwareAcceleration == 1) {
            media.setHWDecoderEnabled(false, false);
        } else if (hardwareAcceleration == 2 || hardwareAcceleration == 0) {
            media.setHWDecoderEnabled(true, true);
            if (hardwareAcceleration == 0) {
                media.addOption(":no-mediacodec-dr");
                media.addOption(":no-omxil-dr");
            }
        }
        if (paused) {
            media.addOption(":start-paused");
        }
    }

    @MainThread
    public static Equalizer getEqualizer(Context context) {
        Equalizer equalizer = null;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref.getBoolean("equalizer_enabled", false)) {
            float[] bands = Preferences.getFloatArray(pref, "equalizer_values");
            int bandCount = Equalizer.getBandCount();
            if (bands.length == bandCount + 1) {
                equalizer = Equalizer.create();
                equalizer.setPreAmp(bands[0]);
                for (int i = 0; i < bandCount; i++) {
                    equalizer.setAmp(i, bands[i + 1]);
                }
            }
        }
        return equalizer;
    }

    public static int getEqualizerPreset(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("equalizer_preset", 0);
    }

    public static void setEqualizer(Context context, Equalizer eq, int preset) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (eq != null) {
            editor.putBoolean("equalizer_enabled", true);
            int bandCount = Equalizer.getBandCount();
            float[] bands = new float[(bandCount + 1)];
            bands[0] = eq.getPreAmp();
            for (int i = 0; i < bandCount; i++) {
                bands[i + 1] = eq.getAmp(i);
            }
            Preferences.putFloatArray(editor, "equalizer_values", bands);
            editor.putInt("equalizer_preset", preset);
        } else {
            editor.putBoolean("equalizer_enabled", false);
        }
        editor.apply();
    }
}
