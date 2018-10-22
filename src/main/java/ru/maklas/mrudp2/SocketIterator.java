package ru.maklas.mrudp2;

/**
 * Helps to stop receiving data during {@link Socket#update(SocketProcessor)}
 */
public interface SocketIterator {

    /**
     * Call to skip next packet from being received until next {@link Socket#update(SocketProcessor)} method is called
     */
    void stop();

    /**
     * @return Whether this receiving command is interrupted/stopped
     */
    boolean isProcessing();

}
