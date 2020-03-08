package com.common.common;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Slf4j
public class RingBufferWorkerPoolFactory {
    private static class SingletonHolder{
        static final RingBufferWorkerPoolFactory factory = new RingBufferWorkerPoolFactory();
    }
    public RingBufferWorkerPoolFactory() {
    }

    public static RingBufferWorkerPoolFactory getInstance(){
        return SingletonHolder.factory;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWraper> ringBuffer;
    private WorkerPool<TranslatorDataWraper> workerPool;
    private SequenceBarrier sequenceBarrier;

    public void initStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] consumersList){
        //构造 ringbuffer
        this.ringBuffer = RingBuffer.create(type, new EventFactory<TranslatorDataWraper>() {
            @Override
            public TranslatorDataWraper newInstance() {
                return new TranslatorDataWraper();
            }
        }, bufferSize, waitStrategy);

        //设置栅栏
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        //设置workpool
        this.workerPool = new WorkerPool<TranslatorDataWraper>(this.ringBuffer, this.sequenceBarrier, null, consumersList);
        //把所构建的消费者放入池中
        Arrays.asList(consumersList).forEach(messageConsumer -> {
            consumers.put(messageConsumer.getConsumerId(), messageConsumer);
        });

        //添加sequence
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        //启动workpool
        this.workerPool.start(Executors.newFixedThreadPool(10));
    }

    public MessageProducer getMessageProducer(String produceId){
        MessageProducer producer = producers.get(produceId);
        if (null == producer){
            producer = new MessageProducer(this.ringBuffer);
            producers.put(produceId, producer);
        }
        return producer;
    }
}

