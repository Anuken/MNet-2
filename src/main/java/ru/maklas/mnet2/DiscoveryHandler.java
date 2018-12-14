package ru.maklas.mnet2;

import java.net.DatagramPacket;

public interface DiscoveryHandler{
    DatagramPacket writeDiscoveryData();
}
