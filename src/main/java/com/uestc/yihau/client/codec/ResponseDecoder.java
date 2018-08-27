package com.uestc.yihau.client.codec;


import com.uestc.yihau.client.constant.ConstantValue;
import com.uestc.yihau.client.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ResponseDecoder extends ByteToMessageDecoder{
    //数据包基本长度
    public static int BASE_LENTH = 4 + 2 + 2 + 4 + 4;

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (true) {
            if (in.readableBytes() >= BASE_LENTH){
                int beginIndex;
                while (true){
                    beginIndex = in.readerIndex();
                    in.markReaderIndex();
                    if (in.readInt() == ConstantValue.FLAG){
                        break;
                    }
                    in.resetReaderIndex();
                    //读到的不是包头，略过一个字节然后读后面的数据。
                    in.readByte();
                    if (in.readableBytes() < BASE_LENTH){
                        return;
                    }
                }
                short moudule = in.readShort();
                short cmd = in.readShort();
                int stateCode = in.readInt();
                int len = in.readInt();
                if (in.readableBytes() < len){
                    in.resetReaderIndex();
                    return;
                }
                byte[] data = new byte[len];
                in.readBytes(data);
                Response response = new Response();
                response.setModule(moudule);
                response.setCmd(cmd);
                response.setStateCode(stateCode);
                response.setData(data);
                out.add(response);
            }else {
                break;
            }
        }
        //数据包不完整等待数据到来
        return;
    }
}
