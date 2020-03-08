package com.netty.nettyclient.client;

import com.common.common.MessageConsumer;
import com.common.common.TranslatorDataWraper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageConsumerImpl extends MessageConsumer {
    public MessageConsumerImpl(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWraper translatorDataWraper) throws Exception {
        String msg = translatorDataWraper.getMsg();
        log.info("来自服务端的消息---------- {}", msg);
        ChannelHandlerContext ctx = translatorDataWraper.getContext();
        ReferenceCountUtil.refCnt(translatorDataWraper);
    }
}
