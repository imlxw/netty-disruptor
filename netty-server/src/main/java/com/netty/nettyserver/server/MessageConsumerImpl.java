package com.netty.nettyserver.server;

import com.common.common.MessageConsumer;
import com.common.common.TranslatorDataWraper;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageConsumerImpl extends MessageConsumer {
    public MessageConsumerImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWraper translatorDataWraper) throws Exception {
      log.info("收到客户端消息----------{}", translatorDataWraper.getMsg());

        ChannelHandlerContext ctx = translatorDataWraper.getContext();
        ctx.channel().writeAndFlush("返回给客户端消息");
    }
}
