package org.succlz123.okplayer.listener;

import com.google.android.exoplayer.text.Cue;
import java.util.List;

public interface CaptionListener {
    void onCues(List<Cue> list);
}
