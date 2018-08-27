package com.uestc.yihau.client.codec;


import com.uestc.yihau.client.constant.ConstantValue;
import com.uestc.yihau.client.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseEncoder extends MessageToByteEncoder<Response> {
    protected void encode(ChannelHandlerContext ctx, Response msg, ByteBuf out) throws Exception {
        out.writeInt(ConstantValue.FLAG);
        out.writeShort(msg.getModule());
        out.writeShort(msg.getCmd());
        out.writeInt(msg.getStateCode());
        int len = msg.getDataLen();
        if (len <= 0){
            out.writeInt(0);
        }else {
            out.writeInt(len);
            out.writeBytes(msg.getData());
        }
    }
}
