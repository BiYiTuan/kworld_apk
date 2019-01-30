package com.google.android.exoplayer.text.tx3g;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Tx3gParser implements SubtitleParser {
    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_TX3G.equals(mimeType);
    }

    public Subtitle parse(InputStream inputStream) throws IOException {
        return new Tx3gSubtitle(new Cue(new DataInputStream(inputStream).readUTF()));
    }
}
