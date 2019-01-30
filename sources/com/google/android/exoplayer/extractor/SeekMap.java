package com.google.android.exoplayer.extractor;

public interface SeekMap {
    public static final SeekMap UNSEEKABLE = new C09151();

    /* renamed from: com.google.android.exoplayer.extractor.SeekMap$1 */
    static class C09151 implements SeekMap {
        C09151() {
        }

        public boolean isSeekable() {
            return false;
        }

        public long getPosition(long timeUs) {
            return 0;
        }
    }

    long getPosition(long j);

    boolean isSeekable();
}
