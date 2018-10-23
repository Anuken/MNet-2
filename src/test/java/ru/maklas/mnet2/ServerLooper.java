package ru.maklas.mnet2;

import java.net.SocketException;

public class ServerLooper implements Runnable {

    ByteServerSocket serverSocket;
    Array<Looper> loopers = new Array<Looper>();

    public ServerLooper(int port, ByteConnectionProcessor auth) throws SocketException {
        this.serverSocket = new ByteServerSocket(new PacketLossUDPSocket(new JavaUDPSocket(port), 80), 512, 15000, 2500, 125, auth);
        new Thread(this).start();
    }


    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            serverSocket.update();

            for (Looper looper : loopers) {
                looper.update();
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {}
        }


    }

    public void addLooper(Looper looper){
        this.loopers.add(looper);
    }
}