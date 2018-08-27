package com.uestc.yihau.client;

import com.uestc.yihau.client.model.Request;
import com.uestc.yihau.client.module.ModuleId;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
@ChannelHandler.Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private static final Request HEATBEAT = Request.valueOf(ModuleId.HEATBEAT, (short) -1, null);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.WRITER_IDLE){
                ctx.writeAndFlush(HEATBEAT);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
