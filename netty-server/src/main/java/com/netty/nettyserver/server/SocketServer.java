package com.netty.nettyserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServer {

    private SocketServer(){

    }

    public static SocketServer getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;

        private SocketServer singleton;


        //jvm保证只调用一次
        Singleton(){
            singleton = new SocketServer();
        }
        public SocketServer getInstance(){
            return singleton;
        }
    }


    public void startServer() throws InterruptedException{
        log.info("---------启动socket服务---------");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new SocketServerInitializer());
            serverBootstrap.bind(8099);
        }finally {
        }
        log.info("---------socket服务启动成功---------");
    }


    public static void main(String[] args) throws InterruptedException {
        SocketServer.getInstance().startServer();
    }
}
