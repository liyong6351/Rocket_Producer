package com.bwton.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Author liyong
 * @Description 主要用于测试普通消息
 * @Date  2018-12-11 18:50
 **/
public class CommonMsg implements Runnable{
    private String name;
    @Override
    public void run() {
        System.out.println(name);
        DefaultMQProducer producer = new DefaultMQProducer("concurrent_producer");
        producer.setNamesrvAddr("10.1.54.121:9876;10.1.54.122:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        //发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message("TopicTestConcurrent","TagA",("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                //TODO 通过配置参数获取是否需要使用延迟消息
                msg.setDelayTimeLevel(3);
                //调用producer的send()方法发送消息
                //这里调用的是同步的方式，所以会有返回结果，同时默认发送的也是普通消息
                SendResult sendResult = producer.send(msg);

                //打印返回结果，可以看到消息发送的状态以及一些相关信息
                System.out.println(sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
