package com.google.android.exoplayer.smoothstreaming;

import android.content.Context;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingTrackSelector.Output;
import java.io.IOException;
import java.util.Arrays;

public final class DefaultSmoothStreamingTrackSelector implements SmoothStreamingTrackSelector {
    private final Context context;
    private final int streamElementType;

    public DefaultSmoothStreamingTrackSelector(Context context, int streamElementType) {
        this.context = context;
        this.streamElementType = streamElementType;
    }

    public void selectTracks(SmoothStreamingManifest manifest, Output output) throws IOException {
        for (int i = 0; i < manifest.streamElements.length; i++) {
            if (manifest.streamElements[i].type == this.streamElementType) {
                int j;
                if (this.streamElementType == 1) {
                    int[] trackIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, Arrays.asList(manifest.streamElements[i].tracks), null, false);
                    if (trackCount > 1) {
                        output.adaptiveTrack(manifest, i, trackIndices);
                    }
                    for (int fixedTrack : trackIndices) {
                        output.fixedTrack(manifest, i, fixedTrack);
                    }
                } else {
                    for (j = 0; j < manifest.streamElements[i].tracks.length; j++) {
                        output.fixedTrack(manifest, i, j);
                    }
                }
            }
        }
    }
}
