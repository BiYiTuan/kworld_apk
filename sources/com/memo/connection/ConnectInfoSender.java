package com.memo.connection;

import android.os.Build;
import android.text.TextUtils;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.memo.TestXlog;
import com.memo.cable.DeviceContainer;
import com.memo.cable.MemoStatusPacket;
import com.memo.cable.ShellUtils;
import com.memo.cable.ShellUtils.CommandResult;
import com.memo.sdk.MemoTVCastSDK;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.cybergarage.upnp.Device;
import org.json.JSONObject;

public class ConnectInfoSender {
    static DatagramSocket receiveSocket = null;
    public static HashMap<String, MemoStatusPacket> sDevicePacketMap = new HashMap();
    static ConnectInfoSender sSender;
    static DatagramSocket sendSocket = null;
    static int server_port_receive = 50001;
    static int server_port_send = 50000;
    public List<String> localPacketMessages = new ArrayList();
    ReceiveApThread mReceiveApThread;
    RecycleSendPackageThread mRecycleSendPackageThread;

    /* renamed from: com.memo.connection.ConnectInfoSender$1 */
    class C07331 implements Runnable {
        C07331() {
        }

        public void run() {
            boolean z = true;
            int i = 0;
            while (z && i < 100) {
                byte[] bArr = new byte[256];
                int i2 = i + 1;
                DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                try {
                    ConnectInfoSender.sendSocket.receive(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String trim = new String(datagramPacket.getData()).trim();
                TestXlog.i2("message[] recevice:" + trim + "!!!!!!!!!!");
                if (!TextUtils.isEmpty(trim) && trim.contains("ssid") && trim.contains("}")) {
                    if (ConnectInfoSender.this.localPacketMessages.contains(trim)) {
                        i = i2;
                    } else {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (MemoTVCastSDK.getISetTvWifiListener() != null) {
                            MemoTVCastSDK.getISetTvWifiListener().onApStateChanged(null, false);
                        }
                        z = false;
                    }
                }
                i = i2;
            }
        }
    }

    public class ReceiveApThread extends Thread {
        public boolean stop = false;

        private void catchException(Exception exception) {
            if (!this.stop) {
                checkDeviceTimeOut();
                TestXlog.i2("Exception:" + exception.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void checkDeviceTimeOut() {
            TestXlog.i2("checkDeviceTimeOut()");
            HashMap hashMap = ConnectInfoSender.sDevicePacketMap;
            if (hashMap != null && hashMap.size() > 0) {
                for (Entry entry : hashMap.entrySet()) {
                    String str = (String) entry.getKey();
                    MemoStatusPacket memoStatusPacket = (MemoStatusPacket) entry.getValue();
                    long currentTimeMillis = System.currentTimeMillis();
                    if (memoStatusPacket.status == 3 && Math.abs(currentTimeMillis - memoStatusPacket.localTime) > 10000) {
                        TestXlog.i2("(curtime - packet.localTime) > 10000");
                        List devices = DeviceContainer.getInstance().getDevices();
                        if (devices.size() > 0) {
                            int i = 0;
                            while (i < devices.size()) {
                                Device device = (Device) devices.get(i);
                                if (TextUtils.equals(device.getChipId(), str)) {
                                    TestXlog.i2("bingo device.getChipId() is " + str);
                                    long currentTimeMillis2 = System.currentTimeMillis();
                                    CommandResult execCmd = ShellUtils.execCmd("ping -c 2 -w 6 " + memoStatusPacket.ip, false);
                                    TestXlog.i2("ping result is " + execCmd.result + ", use time:" + (System.currentTimeMillis() - currentTimeMillis2));
                                    if (execCmd.result != 0) {
                                        TestXlog.i2("remove device : " + device.getFriendlyName());
                                        DeviceContainer.getInstance().removeDevice(device);
                                    } else {
                                        memoStatusPacket.localTime = System.currentTimeMillis();
                                    }
                                } else {
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        }

        public void close() {
            this.stop = true;
            interrupt();
        }

        public void run() {
            while (!this.stop) {
                try {
                    if (ConnectInfoSender.receiveSocket == null || ConnectInfoSender.receiveSocket.isClosed()) {
                        ConnectInfoSender.this.initReceiveSocket();
                    }
                    byte[] bArr = new byte[256];
                    DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                    ConnectInfoSender.receiveSocket.receive(datagramPacket);
                    Object trim = new String(datagramPacket.getData()).trim();
                    TestXlog.i2("receiveSocket receive message is :" + trim);
                    if (!(ConnectInfoSender.this.localPacketMessages.contains(trim) || TextUtils.isEmpty(trim))) {
                        JSONObject jSONObject = new JSONObject(trim);
                        if (jSONObject.has("status")) {
                            boolean z;
                            CharSequence charSequence;
                            List devices;
                            int i;
                            Device device;
                            MemoStatusPacket memoStatusPacket = new MemoStatusPacket(jSONObject);
                            int i2 = memoStatusPacket.status;
                            if (!TextUtils.isEmpty(memoStatusPacket.chipId)) {
                                MemoStatusPacket memoStatusPacket2 = (MemoStatusPacket) ConnectInfoSender.sDevicePacketMap.get(memoStatusPacket.chipId);
                                ConnectInfoSender.sDevicePacketMap.put(memoStatusPacket.chipId, memoStatusPacket);
                                if (memoStatusPacket2 != null) {
                                    if (!TextUtils.equals(memoStatusPacket2.timestamp, memoStatusPacket.timestamp)) {
                                        if (memoStatusPacket.status == memoStatusPacket2.status) {
                                            z = false;
                                            if (MemoTVCastSDK.getISetTvWifiListener() != null) {
                                                MemoTVCastSDK.getISetTvWifiListener().onApStateChanged(memoStatusPacket, z);
                                            }
                                            if (i2 == 3) {
                                                charSequence = memoStatusPacket.chipId;
                                                devices = DeviceContainer.getInstance().getDevices();
                                                for (i = 0; i < devices.size(); i++) {
                                                    device = (Device) devices.get(i);
                                                    if (TextUtils.equals(device.getChipId(), charSequence)) {
                                                        DeviceContainer.getInstance().removeDevice(device);
                                                    }
                                                }
                                            } else {
                                                checkDeviceTimeOut();
                                            }
                                        }
                                    }
                                }
                            }
                            z = true;
                            if (MemoTVCastSDK.getISetTvWifiListener() != null) {
                                MemoTVCastSDK.getISetTvWifiListener().onApStateChanged(memoStatusPacket, z);
                            }
                            if (i2 == 3) {
                                checkDeviceTimeOut();
                            } else {
                                charSequence = memoStatusPacket.chipId;
                                devices = DeviceContainer.getInstance().getDevices();
                                for (i = 0; i < devices.size(); i++) {
                                    device = (Device) devices.get(i);
                                    if (TextUtils.equals(device.getChipId(), charSequence)) {
                                        DeviceContainer.getInstance().removeDevice(device);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    catchException(e);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    catchException(e2);
                } catch (Exception e22) {
                    catchException(e22);
                }
            }
            if (ConnectInfoSender.receiveSocket != null) {
                ConnectInfoSender.receiveSocket.close();
                ConnectInfoSender.receiveSocket = null;
            }
        }
    }

    public class RecycleSendPackageThread extends Thread {
        public String[] sendMessages;
        public boolean stop = false;

        public void appendMessage(String[] strArr) {
            synchronized (ConnectInfoSender.class) {
                this.sendMessages = strArr;
            }
        }

        public void close() {
            this.stop = true;
            interrupt();
        }

        public void run() {
            while (!this.stop) {
                synchronized (ConnectInfoSender.class) {
                    if (this.sendMessages != null) {
                        ConnectInfoSender.this.send(this.sendMessages);
                        this.sendMessages = null;
                    } else {
                        String format = String.format("\"name\":\"%s\",\"timestamp\":\"%s\"", new Object[]{Build.BRAND + "#" + Build.MODEL, ConnectInfoSender.this.getTimestamp()});
                        if (!ConnectInfoSender.this.localPacketMessages.contains(format)) {
                            ConnectInfoSender.this.localPacketMessages.add(format);
                        }
                        ConnectInfoSender.this.send(format);
                    }
                }
                try {
                    Thread.sleep(HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ConnectInfoSender() {
    }

    public static ConnectInfoSender getInstance() {
        ConnectInfoSender connectInfoSender;
        synchronized (ConnectInfoSender.class) {
            if (sSender == null) {
                sSender = new ConnectInfoSender();
            }
            connectInfoSender = sSender;
        }
        return connectInfoSender;
    }

    private String getTimestamp() {
        String valueOf = String.valueOf(System.currentTimeMillis());
        return valueOf.substring(0, valueOf.length() - 3);
    }

    private void initReceiveSocket() {
        try {
            receiveSocket = new DatagramSocket(server_port_receive);
            receiveSocket.setReuseAddress(true);
            TestXlog.i2("init receive socket success");
        } catch (SocketException e) {
            e.printStackTrace();
            TestXlog.i2("init receive socket Exception:" + e.getMessage());
        }
    }

    private void initSendSocket() {
        try {
            sendSocket = new DatagramSocket(server_port_send);
            sendSocket.setReuseAddress(true);
            TestXlog.i2("init send socket success");
        } catch (SocketException e) {
            TestXlog.i2("init send socket error :" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("local Address or local port 50000 is in use now!");
        }
    }

    public void exit() {
        if (this.mRecycleSendPackageThread != null) {
            this.mRecycleSendPackageThread.close();
            this.mRecycleSendPackageThread = null;
        }
        if (this.mReceiveApThread != null) {
            this.mReceiveApThread.close();
            this.mReceiveApThread = null;
        }
        if (sendSocket != null) {
            sendSocket.close();
            sendSocket = null;
        }
        if (receiveSocket != null) {
            receiveSocket.close();
            receiveSocket = null;
        }
    }

    public void interActiveCase0() {
        if (this.mRecycleSendPackageThread != null) {
            this.mRecycleSendPackageThread.stop = true;
            this.mRecycleSendPackageThread.interrupt();
        }
        this.mRecycleSendPackageThread = new RecycleSendPackageThread();
        this.mRecycleSendPackageThread.start();
    }

    public void interActiveCase1(String str, String str2, String str3, int i) {
        if (!WifiStepsConfig.isPureSearch()) {
            TestXlog.i2("interActiveCase1()");
            String timestamp = getTimestamp();
            String format = String.format("{\"name\":\"%s\",\"ssid\":\"%s\",\"key\":\"%s\",\"device_name\":\"%s\",\"mode\":\"%d\",\"timestamp\":\"%s\"}", new Object[]{Build.BRAND + "#" + Build.MODEL, str, str2, str3, Integer.valueOf(i), timestamp});
            timestamp = String.format("{\"ssid\":\"%s\",\"key\":\"%s\",\"device_name\":\"%s\",\"mode\":\"%d\",\"id\":\"%s\"}", new Object[]{str, str2, str3, Integer.valueOf(i), timestamp});
            if (!this.localPacketMessages.contains(timestamp)) {
                this.localPacketMessages.add(timestamp);
            }
            if (!this.localPacketMessages.contains(format)) {
                this.localPacketMessages.add(format);
            }
            String[] strArr = new String[]{format, timestamp};
            TestXlog.i2("json will be send:" + format);
            TestXlog.i2("json will be send:" + timestamp);
            synchronized (ConnectInfoSender.class) {
                if (this.mRecycleSendPackageThread == null || this.mRecycleSendPackageThread.stop) {
                    this.mRecycleSendPackageThread = new RecycleSendPackageThread();
                    this.mRecycleSendPackageThread.appendMessage(strArr);
                    this.mRecycleSendPackageThread.start();
                } else {
                    this.mRecycleSendPackageThread.appendMessage(strArr);
                }
            }
        }
    }

    public boolean isAlive() {
        return this.mRecycleSendPackageThread != null;
    }

    public DatagramSocket send(String str) {
        if (sendSocket == null || sendSocket.isClosed()) {
            initSendSocket();
        }
        TestXlog.i2("DatagramSocket send:" + str);
        if (str == null) {
            str = "Hello IdeasAndroid!";
        }
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] bArr = new byte[0];
        DatagramPacket datagramPacket = new DatagramPacket(str.getBytes(), str.length(), inetAddress, server_port_send);
        int i = 0;
        while (i < 3) {
            try {
                sendSocket.send(datagramPacket);
                TestXlog.i2("udpsocket send 255.255.255.255");
                i++;
            } catch (IOException e2) {
                e2.printStackTrace();
                TestXlog.i2("send io exception:" + e2.getMessage());
            }
        }
        return sendSocket;
    }

    public void send(String[] strArr) {
        if (sendSocket == null || sendSocket.isClosed()) {
            initSendSocket();
        }
        if (strArr != null) {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName("255.255.255.255");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            for (String str : strArr) {
                TestXlog.i2("DatagramSocket send:" + str);
                DatagramPacket datagramPacket = new DatagramPacket(str.getBytes(), str.length(), inetAddress, server_port_send);
                try {
                    sendSocket.send(datagramPacket);
                    sendSocket.send(datagramPacket);
                    sendSocket.send(datagramPacket);
                    TestXlog.i2("DatagramSocket send success");
                    if (str.startsWith("{\"ssid\":")) {
                        new Thread(new C07331()).start();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    TestXlog.i2("send io exception:" + e2.getMessage());
                }
            }
        }
    }

    public void startDeviceApWork() {
        if (!WifiStepsConfig.isPureSearch()) {
            synchronized (ConnectInfoSender.class) {
                if (sendSocket == null) {
                    initSendSocket();
                }
                if (receiveSocket == null) {
                    initReceiveSocket();
                }
            }
            interActiveCase0();
            startReceive();
        }
    }

    public void startReceive() {
        TestXlog.i2("startReceive()");
        if (this.mReceiveApThread != null) {
            this.mReceiveApThread.stop = true;
            this.mReceiveApThread.interrupt();
        }
        this.mReceiveApThread = new ReceiveApThread();
        this.mReceiveApThread.start();
    }
}
