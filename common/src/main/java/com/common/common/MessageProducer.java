package com.common.common;

import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MessageProducer {


    private RingBuffer<TranslatorDataWraper> ringBuffer;


    public void sendData(ChannelHandlerContext context, String msg){
        long seq = ringBuffer.next();
        TranslatorDataWraper translatorDataWraper = ringBuffer.get(seq);
        translatorDataWraper.setContext(context);
        translatorDataWraper.setMsg(msg);
        ringBuffer.publish(seq);
    }
}
