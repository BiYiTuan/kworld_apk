package com.gemini.custom;

import com.gemini.play.MGplayer;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public void send(String url, int port, String context) {
        try {
            MGplayer.MyPrintln("TCP");
            Socket s = new Socket(InetAddress.getByName(url), port);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeBytes(context);
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
