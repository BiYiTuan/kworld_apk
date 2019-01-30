package org.cybergarage.util;

public final class TimerUtil {
    public static final void wait(int i) {
        try {
            Thread.sleep((long) i);
        } catch (Exception e) {
        }
    }

    public static final void waitRandom(int i) {
        try {
            Thread.sleep((long) ((int) (Math.random() * ((double) i))));
        } catch (Exception e) {
        }
    }
}
