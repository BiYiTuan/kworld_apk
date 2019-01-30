package com.gemini.custom;

import com.gemini.play.MGplayer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient {
    private static final int SERVER_PORT = 7005;
    private DatagramSocket dSocket = null;
    private String msg;

    public UdpClient(String msg) {
        this.msg = msg;
    }

    public String send(String ip) {
        StringBuilder sb = new StringBuilder();
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ip);
            sb.append("").append("\n");
        } catch (UnknownHostException e) {
            sb.append("").append("\n");
            e.printStackTrace();
        }
        try {
            this.dSocket = new DatagramSocket();
            sb.append("").append("\n");
        } catch (SocketException e2) {
            e2.printStackTrace();
            sb.append("").append("\n");
        }
        try {
            this.dSocket.send(new DatagramPacket(this.msg.getBytes(), this.msg == null ? 0 : this.msg.length(), inetAddress, SERVER_PORT));
            sb.append("").append("\n");
        } catch (IOException e3) {
            e3.printStackTrace();
            sb.append("").append("\n");
        }
        this.dSocket.close();
        return sb.toString();
    }

    public String send(String ip, int port) {
        StringBuilder sb = new StringBuilder();
        InetAddress rmd = null;
        if (ip == null) {
            return null;
        }
        try {
            rmd = InetAddress.getByName(ip);
            sb.append("").append("\n");
        } catch (UnknownHostException e) {
            sb.append("").append("\n");
            e.printStackTrace();
        }
        try {
            this.dSocket = new DatagramSocket();
            sb.append("").append("\n");
            if (rmd == null) {
                return null;
            }
            try {
                this.dSocket.send(new DatagramPacket(this.msg.getBytes(), this.msg == null ? 0 : this.msg.length(), rmd, port));
                sb.append("").append("\n");
            } catch (IOException e2) {
                e2.printStackTrace();
                sb.append("").append("\n");
            }
            this.dSocket.close();
            MGplayer.MyPrintln(sb.toString());
            return sb.toString();
        } catch (SocketException e3) {
            e3.printStackTrace();
            sb.append("").append("\n");
            return null;
        }
    }

    public String send(String ip, int port, String context) {
        StringBuilder sb = new StringBuilder();
        InetAddress rmd = null;
        if (ip == null) {
            return null;
        }
        try {
            rmd = InetAddress.getByName(ip);
            sb.append("").append("\n");
        } catch (UnknownHostException e) {
            sb.append("").append("\n");
            e.printStackTrace();
        }
        try {
            this.dSocket = new DatagramSocket();
            sb.append("").append("\n");
            if (rmd == null) {
                return null;
            }
            try {
                this.dSocket.send(new DatagramPacket(context.getBytes(), context == null ? 0 : context.length(), rmd, port));
                sb.append("").append("\n");
            } catch (IOException e2) {
                e2.printStackTrace();
                sb.append("").append("\n");
            }
            this.dSocket.close();
            MGplayer.MyPrintln(sb.toString());
            return sb.toString();
        } catch (SocketException e3) {
            e3.printStackTrace();
            sb.append("").append("\n");
            return null;
        }
    }
}
