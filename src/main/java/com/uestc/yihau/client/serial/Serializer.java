package com.uestc.yihau.client.serial;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Serializer {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    protected ByteBuf writeBuffer;
    protected ByteBuf readBuffer;

    protected abstract void read();
    protected abstract void write();


    public byte[] getBytes(){
        writeToLocalBuff();
        byte[] bytes = null;
        if (writeBuffer.writerIndex() == 0){
            bytes = new byte[0];
        }else {
            bytes = new byte[writeBuffer.writerIndex()];
            writeBuffer.readBytes(bytes);
        }
        writeBuffer.clear();
        ReferenceCountUtil.release(writeBuffer);
        return bytes;
    }

    public short readShort(){
        return readBuffer.readShort();
    }

    public int readInt(){
        return readBuffer.readInt();
    }

    public long readLong(){
        return readBuffer.readLong();
    }

    public boolean readBoolean(){
        return readBuffer.readBoolean();
    }
    public char readChar(){
        return readBuffer.readChar();
    }

    public float readFloat(){
        return readBuffer.readFloat();
    }
    public double readDouble(){
        return readBuffer.readDouble();
    }

    public String readString(){
        int size = readBuffer.readShort();
        if (size <= 0){
            return "";
        }
        byte[] bytes = new byte[size];
        readBuffer.readBytes(bytes);
        return new String(bytes, CHARSET);
    }

    public <T> List<T> readList(Class<T> clz){
        List<T> list = new ArrayList<T>();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++){
            list.add(readObject(clz));
        }
        return list;
    }

    public <K,V> Map<K,V> readMap(Class<K> kClass, Class<V> vClass){
        Map<K,V> map = new HashMap<K, V>();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++){
            K key = readObject(kClass);
            V value = readObject(vClass);
            map.put(key, value);
        }
        return map;
    }

    public <I> I readObject(Class<I> clz){
        Object t = null;
        //基本类型得分别处理
        if(clz == int.class || clz == Integer.class){
            t = readBuffer.readInt();
        }else if (clz == byte.class || clz == Byte.class){
            t = readBuffer.readByte();
        }else if (clz == boolean.class || clz == Boolean.class){
            t = readBuffer.readBoolean();
        }else if (clz == short.class || clz == Short.class){
            t = readBuffer.readShort();
        }else if (clz == float.class || clz == Float.class){
            t = readBuffer.readFloat();
        }else if (clz == double.class || clz == Double.class){
            t = readBuffer.readDouble();
        }else if (clz == char.class || clz == Character.class){
            t = readBuffer.readChar();
        }else if (clz == String.class){
            t = readString();
        }else if (Serializer.class.isAssignableFrom(clz)){
            try {
                //对应着写入的时候先读一个byte，判断  1-->有对象，读出来。0-->对象为null；
                byte hasObject = this.readBuffer.readByte();
                if (hasObject == 1){
                    Serializer temp = (Serializer) clz.newInstance();
                    temp.readFromBuffer(this.readBuffer);
                    t = temp;
                }else {
                    t = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new RuntimeException(String.format("不支持的类型:[%s]",clz));
        }
        return (I) t;
    }

    public byte readByte(){
        return readBuffer.readByte();
    }

    public Serializer writeByte(Byte value) {
        writeBuffer.writeByte(value);
        return this;
    }

    public Serializer writeShort(Short value) {
        writeBuffer.writeShort(value);
        return this;
    }

    public Serializer writeInt(Integer value) {
        writeBuffer.writeInt(value);
        return this;
    }

    public Serializer writeLong(Long value) {
        writeBuffer.writeLong(value);
        return this;
    }

    public Serializer writeFloat(Float value) {
        writeBuffer.writeFloat(value);
        return this;
    }

    public Serializer writeDouble(Double value) {
        writeBuffer.writeDouble(value);
        return this;
    }

    public Serializer writeBoolean(Boolean value){
        writeBuffer.writeBoolean(value);
        return this;
    }
    public Serializer writeCharacter(Character value){
        writeBuffer.writeChar(value);
        return this;
    }

    public Serializer writeString(String value){
        if (value == null || value.isEmpty()){
            writeShort((short)0);
            return this;
        }
        byte[] data = value.getBytes();
        short len = (short) data.length;
        writeBuffer.writeShort(len);
        writeBuffer.writeBytes(data);
        return this;
    }

    public <T> Serializer writeList(List<T> list){
        if (list.isEmpty() || list == null){
            writeBuffer.writeShort((short)0);
            return this;
        }
        writeBuffer.writeShort(list.size());
        for (T item : list){
            writeObject(item);
        }
        return this;
    }

    public <K,V> Serializer writeMap(Map<K,V> map){
        if (map.isEmpty() || map == null){
            writeBuffer.writeShort((short)0);
            return this;
        }
        writeBuffer.writeShort((short)map.size());
        for (Map.Entry<K,V> entry : map.entrySet()){
            writeObject(entry.getKey());
            writeObject(entry.getValue());
        }
        return this;
    }

    private Serializer writeObject(Object obj) {
        if (obj == null){
            //对象为null,写入一个0；
            writeByte((byte)0);
        }else {
            if (obj instanceof Integer){
                writeInt((Integer) obj);
                return this;
            }
            if (obj instanceof Short){
                writeShort((Short) obj);
                return this;
            }
            if (obj instanceof Long){
                writeLong((Long) obj);
                return this;
            }
            if (obj instanceof Float){
                writeFloat((Float) obj);
                return this;
            }
            if (obj instanceof Double){
                writeDouble((Double) obj);
                return this;
            }
            if (obj instanceof Boolean){
                writeBoolean((Boolean) obj);
                return this;
            }
            if (obj instanceof Character){
                writeCharacter((Character) obj);
                return this;
            }
            if (obj instanceof Byte){
                writeByte((Byte) obj);
                return this;
            }
            if (obj instanceof String){
                String value = (String) obj;
                writeString(value);
                return this;
            }
            if (obj instanceof Serializer){
                //对象为基本类型之外的对象写入1，然后写入对象。
                writeByte((byte)1);
                Serializer value = (Serializer) obj;
                value.writeToTargetBuff(writeBuffer);
                return this;
            }
            throw new RuntimeException("不可序列化的类型：" + obj.getClass());
        }
        return this;
    }

    protected ByteBuf writeToTargetBuff(ByteBuf buf){
        writeBuffer = buf;
        write();
        return writeBuffer;
    }

    public ByteBuf writeToLocalBuff(){
        writeBuffer = BufferFactory.getBuffer();
        write();
        return writeBuffer;
    }

    public void readFromBuffer(ByteBuf readBuffer) {
        this.readBuffer = readBuffer;
        read();
    }

    public Serializer readFromBytes(byte[] bytes){
        readBuffer = BufferFactory.getBuffer(bytes);
        read();
        readBuffer.clear();
        //释放buffer，此时，数据被读到Serializer的子类的相关域中了。
        ReferenceCountUtil.release(readBuffer);
        return this;
    }

}
