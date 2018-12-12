package com.bwton.rocketmq.producer;

import com.bwton.rocketmq.utils.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * @Author liyong
 * @Description 主要用于测试普通消息
 * @Date 2018-12-11 18:50
 **/
public class CommonMsg {
    private String name;

    public static void executor(DefaultMQProducer producer, String[] tags, String topic, Integer delayType, Integer msgNum,String msgStr) {

        try {
            producer.start();
            //发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
            for (int i = 0; i < msgNum; i++) {
                try {
                    Message msg = new Message(topic, tags[i % tags.length], (msgStr + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    if (delayType > 0) {
                        msg.setDelayTimeLevel(3);
                    }
                    //调用producer的send()方法发送消息
                    //这里调用的是同步的方式，所以会有返回结果，同时默认发送的也是普通消息
                    SendResult sendResult = producer.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
