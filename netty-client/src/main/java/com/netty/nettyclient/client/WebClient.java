package com.netty.nettyclient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WebClient {

    private WebClient(){

    }

    public static WebClient getInstance(){
        return WebClient.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;
        private WebClient singleton;
        Singleton(){
            singleton = new WebClient();
        }
        public WebClient getInstance(){
            return singleton;
        }
    }

    public void connectSocketServer(){
        String domainIp = "127.0.0.1";
        int port = 8099;

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new WebClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect(domainIp, port).sync();
            Channel clientChannel = channelFuture.channel();
            clientChannel.writeAndFlush("message from client-----------");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebClient.getInstance().connectSocketServer();
    }
}
