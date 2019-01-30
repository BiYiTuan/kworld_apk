package com.memo.cable;

import android.text.TextUtils;
import org.json.JSONObject;

public class MemoStatusPacket {
    public static final int STATUS_AP_NORMAL = 1;
    public static final int STATUS_AP_TO_STATION = 2;
    public static final int STATUS_PSW_WRONG = 7001;
    public static final int STATUS_STATION = 3;
    public String apName;
    public String chipId;
    public String deviceName;
    public String ip;
    public long localTime;
    public String pw;
    public String reciveSSID;
    public int status;
    public String timestamp;

    public MemoStatusPacket(JSONObject jSONObject) {
        parse(jSONObject);
    }

    public void parse(JSONObject jSONObject) {
        this.localTime = System.currentTimeMillis();
        this.reciveSSID = jSONObject.optString("ssid");
        if (!(TextUtils.isEmpty(this.reciveSSID) || this.reciveSSID.startsWith("\""))) {
            this.reciveSSID = "\"" + this.reciveSSID + "\"";
        }
        this.deviceName = jSONObject.optString("devname");
        this.pw = jSONObject.optString("pw");
        this.ip = jSONObject.optString("ip");
        if (!TextUtils.isEmpty(this.ip)) {
            this.ip = this.ip.trim();
        }
        this.chipId = jSONObject.optString("chipid");
        this.timestamp = jSONObject.optString("timestamp");
        this.status = jSONObject.optInt("status");
        this.apName = jSONObject.optString("apname");
    }
}
