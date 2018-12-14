package ru.maklas.mnet2.objects;

import ru.maklas.mnet2.Serializer;

public class MySerializer implements Serializer{

    public MySerializer(){

    }

    @Override
    public byte[] serialize(Object o){
        return new byte[0];
    }

    @Override
    public byte[] serialize(Object o, int offset){
        return new byte[0];
    }

    @Override
    public int serialize(Object o, byte[] buffer, int offset){
        return 0;
    }

    @Override
    public Object deserialize(byte[] bytes){
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes, int offset, int length){
        return null;
    }
}
