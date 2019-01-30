package org.succlz123.okplayer.builder;

import org.succlz123.okplayer.OkPlayer;

public interface RendererBuilder {
    void buildRenderers(OkPlayer okPlayer);

    void cancel();
}
