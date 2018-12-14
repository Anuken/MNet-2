package ru.maklas.mnet2.broadcast;

import java.net.InetAddress;

public interface BroadcastProcessor{
    Object process(InetAddress address, int port, Object request);
}
