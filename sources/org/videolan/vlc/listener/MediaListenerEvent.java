package org.videolan.vlc.listener;

public interface MediaListenerEvent {
    void eventBuffing(float f, boolean z);

    void eventError(int i, boolean z);

    void eventPlay(boolean z);

    void eventPlayInit(boolean z);

    void eventStop(boolean z);
}
