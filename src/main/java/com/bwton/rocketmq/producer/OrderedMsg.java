package com.bwton.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @Author liyong
 * @Description 主要用于测试有序消息
 * @Date 2018-12-11 18:50
 **/
public class OrderedMsg {

    public static void executor(DefaultMQProducer producer, String[] tags, String topic, Integer delayType, Integer msgNum, String msgStr) {
        try {
            // 调用start()方法启动一个producer实例
            producer.start();

            // 发送10条消息到Topic为TopicTestOrdered，tag为tags数组按顺序取值，
            // key值为“KEY”拼接上i的值，消息内容为“Hello RocketMQ”拼接上i的值
            for (int i = 0; i < msgNum; i++) {
                int orderId = i % msgNum;
                Message msg = new Message(topic, tags[i % tags.length], "KEY" + i,
                        (msgStr + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                if (delayType > 0) {
                    msg.setDelayTimeLevel(3);
                }
                // 选择发送消息的队列,异步
                producer.send(msg, (mqs, msg1, arg) -> {

                    // arg的值其实就是orderId
                    Integer id = (Integer) arg;

                    // mqs是队列集合，也就是topic所对应的所有队列
                    int index = id % mqs.size();

                    // 这里根据前面的id对队列集合大小求余来返回所对应的队列
                    return mqs.get(index);
                }, orderId);
            }

            producer.shutdown();
        } catch (MQClientException | InterruptedException | MQBrokerException | RemotingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
