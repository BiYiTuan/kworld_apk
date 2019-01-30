package com.memo.sdk;

import com.memo.connection.AccessPoint;
import java.util.List;

public interface IMemoAplistener {
    void onApConnectedTimeOut();

    void onNetworkStateChanged(boolean z);

    void scanFinish(List<AccessPoint> list);
}
