package cn.leo.nativehelper;

public class NativeHelper {
    private static NativeHelper instance = null;

    public native String getDeviceID();

    static {
        try {
            System.loadLibrary("mtdutils");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("WARNING: Could not load library!");
        }
    }

    public static NativeHelper getInstance() {
        if (instance == null) {
            synchronized (NativeHelper.class) {
                if (instance == null) {
                    instance = new NativeHelper();
                }
            }
        }
        return instance;
    }
}
