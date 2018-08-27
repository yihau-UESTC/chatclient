package com.uestc.yihau.client.serial;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.ByteOrder;

/**
 *  bytebuf 工厂类，产生buffer
 */
public class BufferFactory {
    public static ByteOrder  BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    private static ByteBufAllocator bufAllocator = PooledByteBufAllocator.DEFAULT;

    public static ByteBuf getBuffer(){
        ByteBuf buffer = bufAllocator.heapBuffer();
        return buffer;
    }

    public static ByteBuf getBuffer(byte[] bytes){
        ByteBuf buffer =bufAllocator.heapBuffer();
        buffer.writeBytes(bytes);
        return buffer;
    }
}
