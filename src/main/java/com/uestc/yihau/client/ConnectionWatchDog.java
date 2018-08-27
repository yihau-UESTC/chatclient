package com.uestc.yihau.client;

import io.netty.channel.*;
import io.netty.bootstrap.Bootstrap;

import java.util.concurrent.TimeUnit;
@ChannelHandler.Sharable
public abstract class ConnectionWatchDog extends ChannelInboundHandlerAdapter implements Runnable, ChannelHandlerHolder {
    private final Bootstrap bootstrap;
    private final int port;
    private final String host;
    private volatile boolean reconnect = true;
    private int attempts;

    public ConnectionWatchDog(Bootstrap bootstrap, int port, String host, boolean reconnect) {
        this.bootstrap = bootstrap;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("当前链路激活了，重连尝试次数重置为0");
        attempts = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链路关闭");
        if (reconnect){
            if (this.attempts < 12){
                attempts++;
                //指数避让
                int timeout = 2 << attempts;
                ctx.executor().schedule(this, timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void run() {
        ChannelFuture future;
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host, port);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    boolean succeed = future.isSuccess();
                    if (!succeed){
                        System.out.println("重连失败");
                        future.channel().pipeline().fireChannelInactive();
                    }else {
                        System.out.println("重连成功");
                    }
                }
            });

        }
    }
}
