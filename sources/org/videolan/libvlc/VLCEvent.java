package org.videolan.libvlc;

abstract class VLCEvent {
    protected final long arg1;
    protected final float arg2;
    public final int type;

    public interface Listener<T extends VLCEvent> {
        void onEvent(T t);
    }

    protected VLCEvent(int type) {
        this.type = type;
        this.arg1 = 0;
        this.arg2 = 0.0f;
    }

    protected VLCEvent(int type, long arg1) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = 0.0f;
    }

    protected VLCEvent(int type, float arg2) {
        this.type = type;
        this.arg1 = 0;
        this.arg2 = arg2;
    }
}
