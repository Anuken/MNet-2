package ru.maklas.mnet2;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Supplier;
import ru.maklas.mnet2.objects.MySerializer;
import ru.maklas.mnet2.serialization.Serializer;

import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Random;

public class TestUtils {

    public static Supplier<Serializer> serializerSupplier = new MySerializer();


    public static void startUpdating(ServerSocket serverSocket, int freq){
        startUpdating(serverSocket, freq, (s, o) -> {});
    }

    public static void startUpdating(ServerSocket serverSocket, int freq, SocketProcessor processor){
        new Thread(() -> {
            Array<Socket> sockets = new Array<>();
            while (!serverSocket.isClosed()){
                serverSocket.update();
                serverSocket.getSockets(sockets);
                sockets.foreach(s -> s.update(processor));
                try {
                    Thread.sleep(freq);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static ServerSocket newServerSocket(UDPSocket udp, ServerAuthenticator auth){
        return new ServerSocket(udp, 512, 15000, 1500, 125, auth, serializerSupplier);
    }

    public static UDPSocket udp(int port, int additionalPing, int packetLoss) throws SocketException {
        UDPSocket udp = port < 1024 ? new JavaUDPSocket() : new JavaUDPSocket(port);
        if (additionalPing > 0){
            udp = new HighPingUDPSocket(udp, additionalPing);
        }
        if (packetLoss > 0){
            udp = new PacketLossUDPSocket(udp, packetLoss);
        }
        return udp;
    }

    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {}
    }


    public static byte[] randBytes(int len){
        if (len <= 0) return new byte[0];
        byte[] bytes = new byte[len];
        new Random().nextBytes(bytes);
        return bytes;
    }
}