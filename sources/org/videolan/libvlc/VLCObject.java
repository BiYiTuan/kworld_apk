package org.videolan.libvlc;

import android.os.Handler;
import android.os.Looper;
import java.lang.ref.WeakReference;
import org.videolan.libvlc.VLCEvent.Listener;

abstract class VLCObject<T extends VLCEvent> {
    private Listener<T> mEventListener = null;
    private Handler mHandler = null;
    private long mInstance = 0;
    private int mNativeRefCount = 1;

    /* renamed from: org.videolan.libvlc.VLCObject$1EventRunnable */
    class AnonymousClass1EventRunnable implements Runnable {
        private final T event;
        private final Listener<T> listener;

        AnonymousClass1EventRunnable(Listener<T> listener, T event) {
            this.listener = listener;
            this.event = event;
        }

        public void run() {
            this.listener.onEvent(this.event);
        }
    }

    private native void nativeDetachEvents();

    protected abstract T onEventNative(int i, long j, float f);

    protected abstract void onReleaseNative();

    VLCObject() {
    }

    public synchronized boolean isReleased() {
        return this.mNativeRefCount == 0;
    }

    public final synchronized boolean retain() {
        boolean z;
        if (this.mNativeRefCount > 0) {
            this.mNativeRefCount++;
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void release() {
        /*
        r3 = this;
        r0 = -1;
        monitor-enter(r3);
        r2 = r3.mNativeRefCount;	 Catch:{ all -> 0x0028 }
        if (r2 != 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
    L_0x0007:
        return;
    L_0x0008:
        r2 = r3.mNativeRefCount;	 Catch:{ all -> 0x0028 }
        if (r2 <= 0) goto L_0x0013;
    L_0x000c:
        r2 = r3.mNativeRefCount;	 Catch:{ all -> 0x0028 }
        r1 = r2 + -1;
        r3.mNativeRefCount = r1;	 Catch:{ all -> 0x0028 }
        r0 = r1;
    L_0x0013:
        if (r0 != 0) goto L_0x0019;
    L_0x0015:
        r2 = 0;
        r3.setEventListener(r2);	 Catch:{ all -> 0x0028 }
    L_0x0019:
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
        if (r0 != 0) goto L_0x0007;
    L_0x001c:
        r3.nativeDetachEvents();
        monitor-enter(r3);
        r3.onReleaseNative();	 Catch:{ all -> 0x0025 }
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        goto L_0x0007;
    L_0x0025:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        throw r2;
    L_0x0028:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.videolan.libvlc.VLCObject.release():void");
    }

    protected synchronized void setEventListener(Listener<T> listener) {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        this.mEventListener = listener;
        if (this.mEventListener != null && this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
    }

    private synchronized void dispatchEventFromNative(int eventType, long arg1, float arg2) {
        if (!isReleased()) {
            T event = onEventNative(eventType, arg1, arg2);
            if (!(event == null || this.mEventListener == null || this.mHandler == null)) {
                this.mHandler.post(new AnonymousClass1EventRunnable(this.mEventListener, event));
            }
        }
    }

    private Object getWeakReference() {
        return new WeakReference(this);
    }

    private static void dispatchEventFromWeakNative(Object weak, int eventType, long arg1, float arg2) {
        VLCObject obj = (VLCObject) ((WeakReference) weak).get();
        if (obj != null) {
            obj.dispatchEventFromNative(eventType, arg1, arg2);
        }
    }
}
