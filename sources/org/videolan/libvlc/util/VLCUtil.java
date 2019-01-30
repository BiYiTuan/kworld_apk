package org.videolan.libvlc.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

public class VLCUtil {
    private static final String[] CPU_archs = new String[]{"*Pre-v4", "*v4", "*v4T", "v5T", "v5TE", "v5TEJ", "v6", "v6KZ", "v6T2", "v6K", "v7", "*v6-M", "*v6S-M", "*v7E-M", "*v8"};
    private static final int ELF_HEADER_SIZE = 52;
    private static final int EM_386 = 3;
    private static final int EM_AARCH64 = 183;
    private static final int EM_ARM = 40;
    private static final int EM_MIPS = 8;
    private static final int EM_X86_64 = 62;
    private static final int SECTION_HEADER_SIZE = 40;
    private static final int SHT_ARM_ATTRIBUTES = 1879048195;
    public static final String TAG = "VLC/LibVLC/Util";
    private static String errorMsg = null;
    private static boolean isCompatible = false;
    private static MachineSpecs machineSpecs = null;

    private static class ElfData {
        String att_arch;
        boolean att_fpu;
        int e_machine;
        int e_shnum;
        int e_shoff;
        boolean is64bits;
        ByteOrder order;
        int sh_offset;
        int sh_size;

        private ElfData() {
        }
    }

    public static class MachineSpecs {
        public float bogoMIPS;
        public float frequency;
        public boolean hasArmV6;
        public boolean hasArmV7;
        public boolean hasFpu;
        public boolean hasMips;
        public boolean hasNeon;
        public boolean hasX86;
        public boolean is64bits;
        public int processors;
    }

    private static native byte[] nativeGetThumbnail(Media media, int i, int i2);

    public static String getErrorMsg() {
        return errorMsg;
    }

    @TargetApi(21)
    public static String[] getABIList21() {
        String[] abis = Build.SUPPORTED_ABIS;
        if (abis == null || abis.length == 0) {
            return getABIList();
        }
        return abis;
    }

    @TargetApi(8)
    public static String[] getABIList() {
        boolean hasABI2;
        int i;
        if (VERSION.SDK_INT >= 8) {
            hasABI2 = true;
        } else {
            hasABI2 = false;
        }
        if (hasABI2) {
            i = 2;
        } else {
            i = 1;
        }
        String[] abis = new String[i];
        abis[0] = Build.CPU_ABI;
        if (hasABI2) {
            abis[1] = Build.CPU_ABI2;
        }
        return abis;
    }

    public static boolean hasCompatibleCPU(Context context) {
        FileReader fileReader;
        BufferedReader br;
        Reader fileReader2;
        BufferedReader br2;
        Reader fileReader3;
        Throwable th;
        float frequency;
        if (errorMsg != null || isCompatible) {
            return isCompatible;
        }
        String line;
        boolean hasNeon = false;
        boolean hasFpu = false;
        boolean hasArmV6 = false;
        boolean hasPlaceHolder = false;
        boolean hasArmV7 = false;
        boolean hasMips = false;
        boolean hasX86 = false;
        boolean is64bits = false;
        boolean isIntel = false;
        float bogoMIPS = -1.0f;
        int processors = 0;
        String[] abis;
        if (VERSION.SDK_INT >= 21) {
            abis = getABIList21();
        } else {
            abis = getABIList();
        }
        for (String abi : abis) {
            if (abi.equals("x86")) {
                hasX86 = true;
            } else if (abi.equals("x86_64")) {
                hasX86 = true;
                is64bits = true;
            } else if (abi.equals("armeabi-v7a")) {
                hasArmV7 = true;
                hasArmV6 = true;
            } else if (abi.equals("armeabi")) {
                hasArmV6 = true;
            } else if (abi.equals("arm64-v8a")) {
                hasNeon = true;
                hasArmV6 = true;
                hasArmV7 = true;
                is64bits = true;
            }
        }
        ElfData elf = null;
        boolean elfHasX86 = false;
        boolean elfHasArm = false;
        boolean elfHasMips = false;
        boolean elfIs64bits = false;
        File lib = searchLibrary(context.getApplicationInfo());
        if (lib != null) {
            elf = readLib(lib);
            if (elf != null) {
                elfHasX86 = elf.e_machine == 3 || elf.e_machine == 62;
                elfHasArm = elf.e_machine == 40 || elf.e_machine == EM_AARCH64;
                elfHasMips = elf.e_machine == 8;
                elfIs64bits = elf.is64bits;
                String str = TAG;
                StringBuilder append = new StringBuilder().append("ELF ABI = ");
                String str2 = elfHasArm ? "arm" : elfHasX86 ? "x86" : "mips";
                Log.i(str, append.append(str2).append(", ").append(elfIs64bits ? "64bits" : "32bits").toString());
                Log.i(TAG, "ELF arch = " + elf.att_arch);
                Log.i(TAG, "ELF fpu = " + elf.att_fpu);
                fileReader = null;
                br = null;
                fileReader2 = new FileReader("/proc/cpuinfo");
                br2 = new BufferedReader(fileReader2);
                while (true) {
                    try {
                        line = br2.readLine();
                        if (line != null) {
                            break;
                        }
                        if (line.contains("AArch64")) {
                            hasArmV7 = true;
                            hasArmV6 = true;
                        } else if (line.contains("ARMv7")) {
                            hasArmV7 = true;
                            hasArmV6 = true;
                        } else if (line.contains("ARMv6")) {
                            hasArmV6 = true;
                        } else if (line.contains("clflush size")) {
                            hasX86 = true;
                        } else if (line.contains("GenuineIntel")) {
                            hasX86 = true;
                        } else if (line.contains("placeholder")) {
                            hasPlaceHolder = true;
                        } else if (!line.contains("CPU implementer") && line.contains("0x69")) {
                            isIntel = true;
                        } else if (line.contains("microsecond timers")) {
                            hasMips = true;
                        }
                        if (line.contains("neon") || line.contains("asimd")) {
                            hasNeon = true;
                        }
                        if (line.contains("vfp") || (line.contains("Features") && line.contains("fp"))) {
                            hasFpu = true;
                        }
                        if (line.startsWith("processor")) {
                            processors++;
                        }
                        if (bogoMIPS < 0.0f && line.toLowerCase(Locale.ENGLISH).contains("bogomips")) {
                            try {
                                bogoMIPS = Float.parseFloat(line.split(":")[1].trim());
                            } catch (NumberFormatException e) {
                                bogoMIPS = -1.0f;
                            }
                        }
                    } catch (IOException e2) {
                        br = br2;
                        fileReader3 = fileReader2;
                    } catch (Throwable th2) {
                        th = th2;
                        br = br2;
                        fileReader3 = fileReader2;
                    }
                }
                if (br2 != null) {
                    try {
                        br2.close();
                    } catch (IOException e3) {
                    }
                }
                if (fileReader2 == null) {
                    try {
                        fileReader2.close();
                        br = br2;
                        fileReader3 = fileReader2;
                    } catch (IOException e4) {
                        br = br2;
                        fileReader3 = fileReader2;
                    }
                } else {
                    fileReader3 = fileReader2;
                }
                if (processors == 0) {
                    processors = 1;
                }
                isCompatible = true;
                if (elf != null) {
                    if (elfHasX86 || hasX86) {
                        if (elfHasArm && !hasArmV6) {
                            errorMsg = "ARM build on non ARM device";
                            isCompatible = false;
                        }
                    } else if (hasPlaceHolder && isIntel) {
                        Log.d(TAG, "Emulated armv7 detected, trying to launch x86 libraries");
                    } else {
                        errorMsg = "x86 build on non-x86 device";
                        isCompatible = false;
                    }
                    if (!elfHasMips && !hasMips) {
                        errorMsg = "MIPS build on non-MIPS device";
                        isCompatible = false;
                    } else if (elfHasArm && hasMips) {
                        errorMsg = "ARM build on MIPS device";
                        isCompatible = false;
                    }
                    if (elf.e_machine == 40 && elf.att_arch.startsWith("v7") && !hasArmV7) {
                        errorMsg = "ARMv7 build on non-ARMv7 device";
                        isCompatible = false;
                    }
                    if (elf.e_machine == 40) {
                        if (!elf.att_arch.startsWith("v6") && !hasArmV6) {
                            errorMsg = "ARMv6 build on non-ARMv6 device";
                            isCompatible = false;
                        } else if (elf.att_fpu && !hasFpu) {
                            errorMsg = "FPU-enabled build on non-FPU device";
                            isCompatible = false;
                        }
                    }
                    if (elfIs64bits && !is64bits) {
                        errorMsg = "64bits build on 32bits device";
                        isCompatible = false;
                    }
                }
                frequency = -1.0f;
                fileReader = null;
                br = null;
                line = "";
                fileReader2 = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
                try {
                    br2 = new BufferedReader(fileReader2);
                    try {
                        line = br2.readLine();
                        if (line != null) {
                            frequency = Float.parseFloat(line) / 1000.0f;
                        }
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e5) {
                            }
                        }
                        if (fileReader2 == null) {
                            try {
                                fileReader2.close();
                                br = br2;
                                fileReader3 = fileReader2;
                            } catch (IOException e6) {
                                br = br2;
                                fileReader3 = fileReader2;
                            }
                        } else {
                            fileReader3 = fileReader2;
                        }
                    } catch (IOException e7) {
                        br = br2;
                        fileReader3 = fileReader2;
                        try {
                            Log.w(TAG, "Could not find maximum CPU frequency!");
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e8) {
                                }
                            }
                            if (fileReader != null) {
                                try {
                                    fileReader.close();
                                } catch (IOException e9) {
                                }
                            }
                            machineSpecs = new MachineSpecs();
                            Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                            machineSpecs.hasArmV6 = hasArmV6;
                            machineSpecs.hasArmV7 = hasArmV7;
                            machineSpecs.hasFpu = hasFpu;
                            machineSpecs.hasMips = hasMips;
                            machineSpecs.hasNeon = hasNeon;
                            machineSpecs.hasX86 = hasX86;
                            machineSpecs.is64bits = is64bits;
                            machineSpecs.bogoMIPS = bogoMIPS;
                            machineSpecs.processors = processors;
                            machineSpecs.frequency = frequency;
                            return isCompatible;
                        } catch (Throwable th3) {
                            th = th3;
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e10) {
                                }
                            }
                            if (fileReader != null) {
                                try {
                                    fileReader.close();
                                } catch (IOException e11) {
                                }
                            }
                            throw th;
                        }
                    } catch (NumberFormatException e12) {
                        br = br2;
                        fileReader3 = fileReader2;
                        Log.w(TAG, "Could not parse maximum CPU frequency!");
                        Log.w(TAG, "Failed to parse: " + line);
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e13) {
                            }
                        }
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (IOException e14) {
                            }
                        }
                        machineSpecs = new MachineSpecs();
                        Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                        machineSpecs.hasArmV6 = hasArmV6;
                        machineSpecs.hasArmV7 = hasArmV7;
                        machineSpecs.hasFpu = hasFpu;
                        machineSpecs.hasMips = hasMips;
                        machineSpecs.hasNeon = hasNeon;
                        machineSpecs.hasX86 = hasX86;
                        machineSpecs.is64bits = is64bits;
                        machineSpecs.bogoMIPS = bogoMIPS;
                        machineSpecs.processors = processors;
                        machineSpecs.frequency = frequency;
                        return isCompatible;
                    } catch (Throwable th4) {
                        th = th4;
                        br = br2;
                        fileReader3 = fileReader2;
                        if (br != null) {
                            br.close();
                        }
                        if (fileReader != null) {
                            fileReader.close();
                        }
                        throw th;
                    }
                } catch (IOException e15) {
                    fileReader3 = fileReader2;
                    Log.w(TAG, "Could not find maximum CPU frequency!");
                    if (br != null) {
                        br.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    machineSpecs = new MachineSpecs();
                    Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                    machineSpecs.hasArmV6 = hasArmV6;
                    machineSpecs.hasArmV7 = hasArmV7;
                    machineSpecs.hasFpu = hasFpu;
                    machineSpecs.hasMips = hasMips;
                    machineSpecs.hasNeon = hasNeon;
                    machineSpecs.hasX86 = hasX86;
                    machineSpecs.is64bits = is64bits;
                    machineSpecs.bogoMIPS = bogoMIPS;
                    machineSpecs.processors = processors;
                    machineSpecs.frequency = frequency;
                    return isCompatible;
                } catch (NumberFormatException e16) {
                    fileReader3 = fileReader2;
                    Log.w(TAG, "Could not parse maximum CPU frequency!");
                    Log.w(TAG, "Failed to parse: " + line);
                    if (br != null) {
                        br.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    machineSpecs = new MachineSpecs();
                    Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                    machineSpecs.hasArmV6 = hasArmV6;
                    machineSpecs.hasArmV7 = hasArmV7;
                    machineSpecs.hasFpu = hasFpu;
                    machineSpecs.hasMips = hasMips;
                    machineSpecs.hasNeon = hasNeon;
                    machineSpecs.hasX86 = hasX86;
                    machineSpecs.is64bits = is64bits;
                    machineSpecs.bogoMIPS = bogoMIPS;
                    machineSpecs.processors = processors;
                    machineSpecs.frequency = frequency;
                    return isCompatible;
                } catch (Throwable th5) {
                    th = th5;
                    fileReader3 = fileReader2;
                    if (br != null) {
                        br.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    throw th;
                }
                machineSpecs = new MachineSpecs();
                Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                machineSpecs.hasArmV6 = hasArmV6;
                machineSpecs.hasArmV7 = hasArmV7;
                machineSpecs.hasFpu = hasFpu;
                machineSpecs.hasMips = hasMips;
                machineSpecs.hasNeon = hasNeon;
                machineSpecs.hasX86 = hasX86;
                machineSpecs.is64bits = is64bits;
                machineSpecs.bogoMIPS = bogoMIPS;
                machineSpecs.processors = processors;
                machineSpecs.frequency = frequency;
                return isCompatible;
            }
        }
        Log.w(TAG, "WARNING: Unable to read libvlcjni.so; cannot check device ABI!");
        fileReader = null;
        br = null;
        try {
            fileReader2 = new FileReader("/proc/cpuinfo");
            try {
                br2 = new BufferedReader(fileReader2);
                while (true) {
                    line = br2.readLine();
                    if (line != null) {
                        break;
                    }
                    if (line.contains("AArch64")) {
                        hasArmV7 = true;
                        hasArmV6 = true;
                    } else if (line.contains("ARMv7")) {
                        hasArmV7 = true;
                        hasArmV6 = true;
                    } else if (line.contains("ARMv6")) {
                        hasArmV6 = true;
                    } else if (line.contains("clflush size")) {
                        hasX86 = true;
                    } else if (line.contains("GenuineIntel")) {
                        hasX86 = true;
                    } else if (line.contains("placeholder")) {
                        hasPlaceHolder = true;
                    } else {
                        if (!line.contains("CPU implementer")) {
                        }
                        if (line.contains("microsecond timers")) {
                            hasMips = true;
                        }
                    }
                    hasNeon = true;
                    hasFpu = true;
                    if (line.startsWith("processor")) {
                        processors++;
                    }
                    bogoMIPS = Float.parseFloat(line.split(":")[1].trim());
                }
                if (br2 != null) {
                    br2.close();
                }
                if (fileReader2 == null) {
                    fileReader3 = fileReader2;
                } else {
                    fileReader2.close();
                    br = br2;
                    fileReader3 = fileReader2;
                }
            } catch (IOException e17) {
                fileReader3 = fileReader2;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e18) {
                    }
                }
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e19) {
                    }
                }
                if (processors == 0) {
                    processors = 1;
                }
                isCompatible = true;
                if (elf != null) {
                    if (elfHasX86) {
                    }
                    errorMsg = "ARM build on non ARM device";
                    isCompatible = false;
                    if (!elfHasMips) {
                    }
                    errorMsg = "ARM build on MIPS device";
                    isCompatible = false;
                    errorMsg = "ARMv7 build on non-ARMv7 device";
                    isCompatible = false;
                    if (elf.e_machine == 40) {
                        if (!elf.att_arch.startsWith("v6")) {
                        }
                        errorMsg = "FPU-enabled build on non-FPU device";
                        isCompatible = false;
                    }
                    errorMsg = "64bits build on 32bits device";
                    isCompatible = false;
                }
                frequency = -1.0f;
                fileReader = null;
                br = null;
                line = "";
                fileReader2 = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
                br2 = new BufferedReader(fileReader2);
                line = br2.readLine();
                if (line != null) {
                    frequency = Float.parseFloat(line) / 1000.0f;
                }
                if (br2 != null) {
                    br2.close();
                }
                if (fileReader2 == null) {
                    fileReader2.close();
                    br = br2;
                    fileReader3 = fileReader2;
                } else {
                    fileReader3 = fileReader2;
                }
                machineSpecs = new MachineSpecs();
                Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
                machineSpecs.hasArmV6 = hasArmV6;
                machineSpecs.hasArmV7 = hasArmV7;
                machineSpecs.hasFpu = hasFpu;
                machineSpecs.hasMips = hasMips;
                machineSpecs.hasNeon = hasNeon;
                machineSpecs.hasX86 = hasX86;
                machineSpecs.is64bits = is64bits;
                machineSpecs.bogoMIPS = bogoMIPS;
                machineSpecs.processors = processors;
                machineSpecs.frequency = frequency;
                return isCompatible;
            } catch (Throwable th6) {
                th = th6;
                fileReader3 = fileReader2;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e20) {
                    }
                }
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e21) {
                    }
                }
                throw th;
            }
        } catch (IOException e22) {
            if (br != null) {
                br.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            if (processors == 0) {
                processors = 1;
            }
            isCompatible = true;
            if (elf != null) {
                if (elfHasX86) {
                }
                errorMsg = "ARM build on non ARM device";
                isCompatible = false;
                if (!elfHasMips) {
                }
                errorMsg = "ARM build on MIPS device";
                isCompatible = false;
                errorMsg = "ARMv7 build on non-ARMv7 device";
                isCompatible = false;
                if (elf.e_machine == 40) {
                    if (!elf.att_arch.startsWith("v6")) {
                    }
                    errorMsg = "FPU-enabled build on non-FPU device";
                    isCompatible = false;
                }
                errorMsg = "64bits build on 32bits device";
                isCompatible = false;
            }
            frequency = -1.0f;
            fileReader = null;
            br = null;
            line = "";
            fileReader2 = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            br2 = new BufferedReader(fileReader2);
            line = br2.readLine();
            if (line != null) {
                frequency = Float.parseFloat(line) / 1000.0f;
            }
            if (br2 != null) {
                br2.close();
            }
            if (fileReader2 == null) {
                fileReader3 = fileReader2;
            } else {
                fileReader2.close();
                br = br2;
                fileReader3 = fileReader2;
            }
            machineSpecs = new MachineSpecs();
            Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
            machineSpecs.hasArmV6 = hasArmV6;
            machineSpecs.hasArmV7 = hasArmV7;
            machineSpecs.hasFpu = hasFpu;
            machineSpecs.hasMips = hasMips;
            machineSpecs.hasNeon = hasNeon;
            machineSpecs.hasX86 = hasX86;
            machineSpecs.is64bits = is64bits;
            machineSpecs.bogoMIPS = bogoMIPS;
            machineSpecs.processors = processors;
            machineSpecs.frequency = frequency;
            return isCompatible;
        } catch (Throwable th7) {
            th = th7;
            if (br != null) {
                br.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            throw th;
        }
        if (processors == 0) {
            processors = 1;
        }
        isCompatible = true;
        if (elf != null) {
            if (elfHasX86) {
            }
            errorMsg = "ARM build on non ARM device";
            isCompatible = false;
            if (!elfHasMips) {
            }
            errorMsg = "ARM build on MIPS device";
            isCompatible = false;
            errorMsg = "ARMv7 build on non-ARMv7 device";
            isCompatible = false;
            if (elf.e_machine == 40) {
                if (!elf.att_arch.startsWith("v6")) {
                }
                errorMsg = "FPU-enabled build on non-FPU device";
                isCompatible = false;
            }
            errorMsg = "64bits build on 32bits device";
            isCompatible = false;
        }
        frequency = -1.0f;
        fileReader = null;
        br = null;
        line = "";
        try {
            fileReader2 = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            br2 = new BufferedReader(fileReader2);
            line = br2.readLine();
            if (line != null) {
                frequency = Float.parseFloat(line) / 1000.0f;
            }
            if (br2 != null) {
                br2.close();
            }
            if (fileReader2 == null) {
                fileReader3 = fileReader2;
            } else {
                fileReader2.close();
                br = br2;
                fileReader3 = fileReader2;
            }
        } catch (IOException e23) {
            Log.w(TAG, "Could not find maximum CPU frequency!");
            if (br != null) {
                br.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            machineSpecs = new MachineSpecs();
            Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
            machineSpecs.hasArmV6 = hasArmV6;
            machineSpecs.hasArmV7 = hasArmV7;
            machineSpecs.hasFpu = hasFpu;
            machineSpecs.hasMips = hasMips;
            machineSpecs.hasNeon = hasNeon;
            machineSpecs.hasX86 = hasX86;
            machineSpecs.is64bits = is64bits;
            machineSpecs.bogoMIPS = bogoMIPS;
            machineSpecs.processors = processors;
            machineSpecs.frequency = frequency;
            return isCompatible;
        } catch (NumberFormatException e24) {
            Log.w(TAG, "Could not parse maximum CPU frequency!");
            Log.w(TAG, "Failed to parse: " + line);
            if (br != null) {
                br.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            machineSpecs = new MachineSpecs();
            Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
            machineSpecs.hasArmV6 = hasArmV6;
            machineSpecs.hasArmV7 = hasArmV7;
            machineSpecs.hasFpu = hasFpu;
            machineSpecs.hasMips = hasMips;
            machineSpecs.hasNeon = hasNeon;
            machineSpecs.hasX86 = hasX86;
            machineSpecs.is64bits = is64bits;
            machineSpecs.bogoMIPS = bogoMIPS;
            machineSpecs.processors = processors;
            machineSpecs.frequency = frequency;
            return isCompatible;
        }
        machineSpecs = new MachineSpecs();
        Log.d(TAG, "machineSpecs: hasArmV6: " + hasArmV6 + ", hasArmV7: " + hasArmV7 + ", hasX86: " + hasX86 + ", is64bits: " + is64bits);
        machineSpecs.hasArmV6 = hasArmV6;
        machineSpecs.hasArmV7 = hasArmV7;
        machineSpecs.hasFpu = hasFpu;
        machineSpecs.hasMips = hasMips;
        machineSpecs.hasNeon = hasNeon;
        machineSpecs.hasX86 = hasX86;
        machineSpecs.is64bits = is64bits;
        machineSpecs.bogoMIPS = bogoMIPS;
        machineSpecs.processors = processors;
        machineSpecs.frequency = frequency;
        return isCompatible;
    }

    public static MachineSpecs getMachineSpecs() {
        return machineSpecs;
    }

    @TargetApi(9)
    private static File searchLibrary(ApplicationInfo applicationInfo) {
        String[] libraryPaths;
        int i = 0;
        if ((applicationInfo.flags & 1) != 0) {
            libraryPaths = System.getProperty("java.library.path").split(":");
        } else {
            libraryPaths = new String[1];
            if (AndroidUtil.isGingerbreadOrLater()) {
                libraryPaths[0] = applicationInfo.nativeLibraryDir;
            } else {
                libraryPaths[0] = applicationInfo.dataDir + "/lib";
            }
        }
        if (libraryPaths[0] == null) {
            Log.e(TAG, "can't find library path");
            return null;
        }
        int length = libraryPaths.length;
        while (i < length) {
            File lib = new File(libraryPaths[i], "libvlcjni.so");
            if (lib.exists() && lib.canRead()) {
                return lib;
            }
            i++;
        }
        Log.e(TAG, "WARNING: Can't find shared library");
        return null;
    }

    private static ElfData readLib(File file) {
        FileNotFoundException e;
        Throwable th;
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile in = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
            try {
                ElfData elf = new ElfData();
                if (readHeader(in, elf)) {
                    switch (elf.e_machine) {
                        case 3:
                        case 8:
                        case 62:
                        case EM_AARCH64 /*183*/:
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e2) {
                                }
                            }
                            randomAccessFile = in;
                            return elf;
                        case 40:
                            in.close();
                            randomAccessFile = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
                            if (readSection(randomAccessFile, elf)) {
                                randomAccessFile.close();
                                in = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
                                if (readArmAttributes(in, elf)) {
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e3) {
                                        }
                                    }
                                    randomAccessFile = in;
                                    return elf;
                                }
                                if (in != null) {
                                    try {
                                        in.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                randomAccessFile = in;
                                return null;
                            }
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (IOException e5) {
                                }
                            }
                            return null;
                        default:
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e6) {
                                }
                            }
                            randomAccessFile = in;
                            return null;
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e7) {
                    }
                }
                randomAccessFile = in;
                return null;
            } catch (FileNotFoundException e8) {
                e = e8;
                randomAccessFile = in;
                try {
                    e.printStackTrace();
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e9) {
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e10) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e11) {
                e = e11;
                randomAccessFile = in;
                e.printStackTrace();
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e12) {
                    }
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                randomAccessFile = in;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e13) {
            e = e13;
            e.printStackTrace();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            return null;
        } catch (IOException e14) {
            e = e14;
            e.printStackTrace();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            return null;
        }
    }

    private static boolean readHeader(RandomAccessFile in, ElfData elf) throws IOException {
        boolean z = false;
        byte[] bytes = new byte[52];
        in.readFully(bytes);
        if (bytes[0] == Byte.MAX_VALUE && bytes[1] == (byte) 69 && bytes[2] == (byte) 76 && bytes[3] == (byte) 70 && (bytes[4] == (byte) 1 || bytes[4] == (byte) 2)) {
            if (bytes[4] == (byte) 2) {
                z = true;
            }
            elf.is64bits = z;
            elf.order = bytes[5] == (byte) 1 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(elf.order);
            elf.e_machine = buffer.getShort(18);
            elf.e_shoff = buffer.getInt(32);
            elf.e_shnum = buffer.getShort(48);
            return true;
        }
        Log.e(TAG, "ELF header invalid");
        return false;
    }

    private static boolean readSection(RandomAccessFile in, ElfData elf) throws IOException {
        byte[] bytes = new byte[40];
        in.seek((long) elf.e_shoff);
        int i = 0;
        while (i < elf.e_shnum) {
            in.readFully(bytes);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(elf.order);
            if (buffer.getInt(4) != SHT_ARM_ATTRIBUTES) {
                i++;
            } else {
                elf.sh_offset = buffer.getInt(16);
                elf.sh_size = buffer.getInt(20);
                return true;
            }
        }
        return false;
    }

    private static boolean readArmAttributes(RandomAccessFile in, ElfData elf) throws IOException {
        byte[] bytes = new byte[elf.sh_size];
        in.seek((long) elf.sh_offset);
        in.readFully(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(elf.order);
        if (buffer.get() != (byte) 65) {
            return false;
        }
        while (buffer.remaining() > 0) {
            int start_section = buffer.position();
            int length = buffer.getInt();
            if (getString(buffer).equals("aeabi")) {
                while (buffer.position() < start_section + length) {
                    int start = buffer.position();
                    int tag = buffer.get();
                    int size = buffer.getInt();
                    if (tag != 1) {
                        buffer.position(start + size);
                    } else {
                        while (buffer.position() < start + size) {
                            tag = getUleb128(buffer);
                            if (tag == 6) {
                                elf.att_arch = CPU_archs[getUleb128(buffer)];
                            } else if (tag == 27) {
                                getUleb128(buffer);
                                elf.att_fpu = true;
                            } else {
                                tag %= 128;
                                if (tag == 4 || tag == 5 || tag == 32 || (tag > 32 && (tag & 1) != 0)) {
                                    getString(buffer);
                                } else {
                                    getUleb128(buffer);
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return true;
    }

    private static String getString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder(buffer.limit());
        while (buffer.remaining() > 0) {
            char c = (char) buffer.get();
            if (c == '\u0000') {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static int getUleb128(ByteBuffer buffer) {
        int ret = 0;
        int c;
        do {
            ret <<= 7;
            c = buffer.get();
            ret |= c & TransportMediator.KEYCODE_MEDIA_PAUSE;
        } while ((c & 128) > 0);
        return ret;
    }

    public static byte[] getThumbnail(LibVLC libVLC, Uri uri, int i_width, int i_height) {
        if (uri.getLastPathSegment().endsWith(".iso")) {
            uri = Uri.parse("dvdsimple://" + uri.getEncodedPath());
        }
        Media media = new Media(libVLC, uri);
        byte[] bytes = getThumbnail(media, i_width, i_height);
        media.release();
        return bytes;
    }

    public static byte[] getThumbnail(Media media, int i_width, int i_height) {
        media.addOption(":no-audio");
        media.addOption(":no-spu");
        media.addOption(":no-osd");
        media.addOption(":input-fast-seek");
        return nativeGetThumbnail(media, i_width, i_height);
    }
}
