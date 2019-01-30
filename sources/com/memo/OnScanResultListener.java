package com.memo;

import com.memo.connection.AccessPoint;
import java.util.List;

public interface OnScanResultListener {
    void onScanErrorOccurs();

    void onScanResultUpdateChanged(List<AccessPoint> list);
}
