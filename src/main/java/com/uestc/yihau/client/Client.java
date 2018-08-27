package com.uestc.yihau.client;

import com.uestc.yihau.client.codec.RequestEncoder;
import com.uestc.yihau.client.codec.ResponseDecoder;
import com.uestc.yihau.client.model.Request;
import com.uestc.yihau.client.model.Response;
import com.uestc.yihau.client.module.ModuleId;
import com.uestc.yihau.client.module.chat.ChatCmd;
import com.uestc.yihau.client.module.chat.ChatModule;
import com.uestc.yihau.client.module.player.PlayerCmd;
import com.uestc.yihau.client.module.player.PlayerModule;
import com.uestc.yihau.client.scanner.Invoker;
import com.uestc.yihau.client.scanner.InvokerHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        new Client().connect(8888);
    }

    public void connect(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));
        final ConnectionWatchDog watchDog = new ConnectionWatchDog(bootstrap, 8888, "127.0.0.1", true) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        new ResponseDecoder(),
                        new RequestEncoder(),
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        new ConnectorIdleStateTrigger(),
                        this,
                        new ClientHandler()
                };
            }
        };
        ChannelFuture future;
        try {
            synchronized (bootstrap) {
                bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(watchDog.handlers());
                    }
                });
                future = bootstrap.connect("127.0.0.1", 8888);
            }
            future.sync();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("please input order");
                String line = scanner.nextLine();
                if (line.equals("register")) {
                    System.out.println("please input name : ");
                    String playerName = scanner.nextLine();
                    System.out.println("please input password : ");
                    String password = scanner.nextLine();
                    PlayerModule.RegisterRequest registerRequest = PlayerModule.RegisterRequest.newBuilder()
                            .setPlayerName(playerName)
                            .setPassword(password)
                            .build();
                    Request request = Request.valueOf(ModuleId.PLAYER, PlayerCmd.REGISTER_AND_LOGIN, registerRequest.toByteArray());
                    future.channel().writeAndFlush(request);
                }
                if (line.equals("login")) {
                    System.out.println("please input name : ");
                    String playerName = scanner.nextLine();
                    System.out.println("please input password : ");
                    String password = scanner.nextLine();
                    PlayerModule.LoginRequest loginRequest = PlayerModule.LoginRequest.newBuilder()
                            .setPlayerName(playerName)
                            .setPassword(password)
                            .build();
                    Request request = Request.valueOf(ModuleId.PLAYER, PlayerCmd.LOGIN, loginRequest.toByteArray());
                    future.channel().writeAndFlush(request);
                }
                if (line.equals("send")) {
                    System.out.println("please input message : ");
                    String content = scanner.nextLine();
                    ChatModule.PublicMessageRequest publicMessage = ChatModule.PublicMessageRequest.newBuilder()
                            .setContext(content)
                            .build();
                    Request request = Request.valueOf(ModuleId.CHAT, ChatCmd.PUBLIC_CHAT, publicMessage.toByteArray());
                    future.channel().writeAndFlush(request);
                }
                if (line.equals("exit")) {
                    future.channel().close();
                    scanner.close();
                    break;
                }
            }
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new ResponseDecoder());
//                        ch.pipeline().addLast(new RequestEncoder());
//                        ch.pipeline().addLast(new ClientHandler());
//                    }
//                });

//            ChannelFuture future =  bootstrap.connect(new InetSocketAddress("192.168.0.109", port)).sync();

    }

    private class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Channel 激活");
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Channel 失活");
            ctx.fireChannelInactive();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Response response = (Response) msg;
            int resultCode = response.getStateCode();
            short moudleId = response.getModule();
            short cmd = response.getCmd();
            Invoker invoker = InvokerHolder.getInvoker(moudleId, cmd);
            invoker.invoke(resultCode, response.getData());
        }
    }
}
