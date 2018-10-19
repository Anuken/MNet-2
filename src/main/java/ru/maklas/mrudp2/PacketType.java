package ru.maklas.mrudp2;

import java.util.ArrayList;

class PacketType {

    static final byte reliableRequest = 1;
    static final byte reliableAck = 2;
    static final byte unreliable = 3;
    static final byte batch = 4;

    static final byte pingRequest = 6;
    static final byte pingResponse = 7;

    static final byte connectionRequest = 8;
    static final byte connectionResponseOk = 9;
    static final byte connectionResponseError = 10;
    static final byte connectionAcknowledgment = 11;

    static final byte disconnect = 13;




    static void putShort(byte[] bytes, int value, int offset){
        bytes[    offset] = (byte) (value >>> 8);
        bytes[1 + offset] = (byte)  value;
    }

    static int extractShort(byte[] bytes, int offset){
        return
                bytes[offset] << 8             |
                        (bytes[1 + offset] & 0xFF);
    }

    static void putInt(byte[] bytes, int value, int offset) {
        bytes[    offset] = (byte) (value >>> 24);
        bytes[1 + offset] = (byte) (value >>> 16);
        bytes[2 + offset] = (byte) (value >>> 8);
        bytes[3 + offset] = (byte)  value;
    }

    static int extractInt(byte[] bytes, int offset){
        return
                bytes[offset] << 24             |
                        (bytes[1 + offset] & 0xFF) << 16 |
                        (bytes[2 + offset] & 0xFF) << 8  |
                        (bytes[3 + offset] & 0xFF);
    }

    static void putLong(byte[] bytes, long value, int offset) {
        bytes[    offset] = (byte) (value >>> 56);
        bytes[1 + offset] = (byte) (value >>> 48);
        bytes[2 + offset] = (byte) (value >>> 40);
        bytes[3 + offset] = (byte) (value >>> 32);
        bytes[4 + offset] = (byte) (value >>> 24);
        bytes[5 + offset] = (byte) (value >>> 16);
        bytes[6 + offset] = (byte) (value >>> 8);
        bytes[7 + offset] = (byte)  value;
    }

    static long extractLong(byte[] bytes, int offset){
        long result = 0;
        for (int i = offset; i < 8 + offset; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }

    /**
     * @return (byte[] batchRequest, int currentPosition)
     */
    public static Object[] buildSafeBatch(final int seq, byte settings, MRUDPBatch batch, final int pos, int bufferSize) {
        ArrayList<byte[]> array = batch.array;
        int retSize = 6;
        int batchSize = array.size();
        int endIIncluded = pos;
        for (int i = pos; i < batchSize; i++) {
            int length = array.get(i).length;
            if (retSize + length + 2> bufferSize){
                break;
            }
            endIIncluded = i;
            retSize += length + 2;
        }

        if (retSize == 6){
            throw new RuntimeException("Can't fit byte[] if length " + array.get(pos).length + " in bufferSize of length " + bufferSize + ". Make sure it's at least 8 bytes more than source byte[]");
        }

        int safeBatchSize = endIIncluded - pos + 1;
        byte[] ret = new byte[retSize];
        ret[0] = settings;
        putInt(ret, seq, 1);
        ret[5] = (byte) safeBatchSize;
        int position = 6;
        for (int i = pos; i <= endIIncluded; i++) {
            byte[] src = array.get(i);
            int srcLen = src.length;
            putShort(ret, srcLen, position);
            System.arraycopy(src, 0, ret, position + 2, srcLen);
            position += srcLen + 2;
        }
        return new Object[]{ret, Integer.valueOf(endIIncluded + 1)};
    }

    /**
     * Assumes that batch data is correct. Otherwise will throw Runtime exceptions
     */
    public static byte[][] breakBatchDown(byte[] fullData){
        int arrSize = fullData[5];
        byte[][] ret = new byte[arrSize][];
        int pos = 6;
        for (int i = 0; i < arrSize; i++) {
            int packetSize = extractShort(fullData, pos);
            ret[i] = new byte[packetSize];
            System.arraycopy(fullData, pos + 2, ret[i], 0, packetSize);
            pos += packetSize + 2;
        }
        return ret;
    }

}
