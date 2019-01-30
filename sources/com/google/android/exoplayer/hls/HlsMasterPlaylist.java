package com.google.android.exoplayer.hls;

import java.util.Collections;
import java.util.List;

public final class HlsMasterPlaylist extends HlsPlaylist {
    public final List<Variant> subtitles;
    public final List<Variant> variants;

    public HlsMasterPlaylist(String baseUri, List<Variant> variants, List<Variant> subtitles) {
        super(baseUri, 0);
        this.variants = Collections.unmodifiableList(variants);
        this.subtitles = Collections.unmodifiableList(subtitles);
    }
}
