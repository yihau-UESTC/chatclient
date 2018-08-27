package com.uestc.yihau.client.codec;


import com.uestc.yihau.client.constant.ConstantValue;
import com.uestc.yihau.client.model.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class RequestEncoder extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        out.writeInt(ConstantValue.FLAG);
        out.writeShort(msg.getModule());
        out.writeShort(msg.getCmd());
        int len = msg.getDataLen();
        if (len <= 0){
            out.writeInt(0);
        }else {
            out.writeInt(len);
            out.writeBytes(msg.getData());
        }
    }
}
