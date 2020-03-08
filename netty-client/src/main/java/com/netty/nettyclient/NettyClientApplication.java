package com.netty.nettyclient;

import com.common.common.MessageConsumer;
import com.common.common.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.nettyclient.client.MessageConsumerImpl;
import com.netty.nettyclient.client.WebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication {

    public static void main(String[] args) {
        int consumerCount = 10;
        MessageConsumer[] consumers = new MessageConsumer[consumerCount];

        for (int i = 0; i < consumerCount; i++){
            MessageConsumer consumer = new MessageConsumerImpl("client" + i);
            consumers[i] = consumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initStart(ProducerType.MULTI, 1024 * 1024, new YieldingWaitStrategy(), consumers);
        SpringApplication.run(NettyClientApplication.class, args);
        WebClient.getInstance().connectSocketServer();
    }
}
