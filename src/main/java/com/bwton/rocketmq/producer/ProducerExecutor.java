package com.bwton.rocketmq.producer;

import com.bwton.rocketmq.config.ConfigModel;
import com.bwton.rocketmq.enums.MsgTypeEnum;
import com.bwton.rocketmq.utils.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @ClassName com.bwton.rocketmq.producer.ProducerExcutor
 * @Description 消息发送端执行消息发送的逻辑
 * @Author liyong
 * @Date 2018-12-12 09:24
 * @Version 0.0.1
 **/
public class ProducerExecutor {
    public static void executor(ConfigModel model) {
        // 声明并初始化一个producer
        // 需要一个producer group名字作为构造方法的参数，这里为ordered_producer
        DefaultMQProducer producer = new DefaultMQProducer("ordered_producer");

        // 设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr(model.getNameServer());
        String msgStr = StringUtils.getRandomFixedLengthString(model.getMsgLength());
        if (model.getMsgTypeEnum().equals(MsgTypeEnum.COMMON) || model.getMsgTypeEnum().equals(MsgTypeEnum.COMMON_DELAY)) {
            CommonMsg.executor(producer, model.getTags(), model.getTopicId(), model.getDelayType(),model.getMsgCount(), msgStr);
        }else if (model.getMsgTypeEnum().equals(MsgTypeEnum.ORDERED) || model.getMsgTypeEnum().equals(MsgTypeEnum.ORDERED_DELAY)){
            OrderedMsg.executor(producer, model.getTags(), model.getTopicId(), model.getDelayType(),model.getMsgCount(), msgStr);
        }
    }
}
