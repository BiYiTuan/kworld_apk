package org.cybergarage.util;

import android.util.Log;
import java.io.PrintStream;

public final class Debug {
    public static Debug debug = new Debug();
    public static boolean enabled = false;
    private PrintStream out = System.out;

    public static Debug getDebug() {
        return debug;
    }

    public static boolean isOn() {
        return enabled;
    }

    public static final void message(String str) {
        if (enabled) {
            debug.getOut().println("CyberGarage message : " + str);
            Log.d("cyber", "CyberGarage message : " + str);
        }
    }

    public static final void message(String str, String str2) {
        if (enabled) {
            debug.getOut().println("CyberGarage message : ");
            debug.getOut().println(str);
            debug.getOut().println(str2);
            Log.d("cyber", "CyberGarage message : ");
            Log.d("cyber", str);
            Log.d("cyber", str2);
        }
    }

    public static final void off() {
        enabled = false;
    }

    public static final void on() {
        enabled = true;
    }

    public static final void warning(Exception exception) {
        warning(exception.getMessage());
        exception.printStackTrace(debug.getOut());
        Log.d("cyber", exception.getMessage());
    }

    public static final void warning(String str) {
        debug.getOut().println("CyberGarage warning : " + str);
        Log.d("cyber", str);
    }

    public static final void warning(String str, Exception exception) {
        if (exception.getMessage() == null) {
            debug.getOut().println("CyberGarage warning : " + str + " START");
            exception.printStackTrace(debug.getOut());
            debug.getOut().println("CyberGarage warning : " + str + " END");
            return;
        }
        debug.getOut().println("CyberGarage warning : " + str + " (" + exception.getMessage() + ")");
        exception.printStackTrace(debug.getOut());
    }

    public synchronized PrintStream getOut() {
        return this.out;
    }

    public synchronized void setOut(PrintStream printStream) {
        this.out = printStream;
    }
}
