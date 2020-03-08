package com.netty.nettyserver;

import com.common.common.MessageConsumer;
import com.common.common.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.nettyserver.server.MessageConsumerImpl;
import com.netty.nettyserver.server.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyServerApplication {

	public static void main(String[] args) throws InterruptedException {
		int consumerCount = 10;
		MessageConsumer [] consumers = new MessageConsumer[consumerCount];

		for (int i = 0; i < consumerCount; i++){
			MessageConsumer consumer = new MessageConsumerImpl("CODE" + i);
			consumers[i] = consumer;
		}
		RingBufferWorkerPoolFactory.getInstance().initStart(ProducerType.MULTI, 1024 * 1024, new YieldingWaitStrategy(), consumers);

		SocketServer.getInstance().startServer();
		SpringApplication.run(NettyServerApplication.class, args);
	}

}
