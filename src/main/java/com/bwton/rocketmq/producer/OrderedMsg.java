package com.bwton.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @Author liyong
 * @Description 主要用于测试有序消息
 * @Date  2018-12-11 18:50
 **/
public class OrderedMsg {

    public static void main(String[] args) throws UnsupportedEncodingException {
        try {
            // 声明并初始化一个producer
            // 需要一个producer group名字作为构造方法的参数，这里为ordered_producer
            DefaultMQProducer orderedProducer = new DefaultMQProducer("ordered_producer");

            // 设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
            //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
            orderedProducer.setNamesrvAddr("10.1.54.121:9876;10.1.54.122:9876");

            // 调用start()方法启动一个producer实例
            orderedProducer.start();

            // 自定义一个tag数组
            String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};

            // 发送10条消息到Topic为TopicTestOrdered，tag为tags数组按顺序取值，
            // key值为“KEY”拼接上i的值，消息内容为“Hello RocketMQ”拼接上i的值
            for (int i = 0; i < 10; i++) {

                int orderId = i % 10;
                Message msg =
                        new Message("TopicTestOrdered", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
//TODO 通过配置参数获取是否需要使用延迟消息
                msg.setDelayTimeLevel(3);
                // 选择发送消息的队列
                SendResult sendResult = orderedProducer.send(msg, (mqs, msg1, arg) -> {

                    // arg的值其实就是orderId
                    Integer id = (Integer) arg;

                    // mqs是队列集合，也就是topic所对应的所有队列
                    int index = id % mqs.size();

                    // 这里根据前面的id对队列集合大小求余来返回所对应的队列
                    return mqs.get(index);
                }, orderId);

                System.out.println(sendResult);
            }

            orderedProducer.shutdown();
        } catch (MQClientException | InterruptedException | MQBrokerException | RemotingException e) {
            e.printStackTrace();
        }
    }
}
