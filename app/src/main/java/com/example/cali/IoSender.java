package com.example.cali;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class IoSender {

    public Socket socket;

    public void connect(){

        try {
            socket = IO.socket("http://10.0.2.2:9475");

            socket.connect();

            socket.emit("join","Android");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void send(String msg){
        socket.emit("message",msg);
    }

}
