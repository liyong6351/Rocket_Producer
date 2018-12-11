package com.bwton.rocketmq;

import com.bwton.rocketmq.config.ConfigModel;
import com.bwton.rocketmq.utils.MapUtils;
import io.netty.util.internal.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.bwton.rocketmq.utils.MapUtils
 * @Description 用于Jmetter调用进行测试写入RocketMq的测试类
 * @Author liyong
 * @Date 2018-12-11 19:16
 * @Version 0.0.1
 **/
public class RocketTest
{
    /**
     * 参数1 表示发送消息的类型(0无序消息 1有序消息 2延时消息(延时为随机的10秒之内的值))
     * 参数2 表示发送的TopicId
     * 参数3 表示发送的 Tag
     * 参数4 表示发送消息的条数 默认一次执行10000条消息
     * 参数5 表示发送消息的长度 默认一次发送800字符的消息
     * 参数6 表示配置的NameServer 示例:"10.1.54.121:9876;10.1.54.122:9876"
     * @param args 运行消息
     */
    public static void main( String[] args )
    {
        Map<String,Object> map = tranPara2Model(args);
        if(MapUtils.isValueEmpty(map,"msg")){
            System.out.println("参数错误,没有运行测试用例");
        }else{
            //TODO 测试数据
            System.out.println("达到");
        }
    }


    private static Map<String,Object> tranPara2Model(String[] args) {
        Map<String,Object> result = new HashMap<>();
        String msg = checkPara(args);
        result.put("msg",msg);
        if(StringUtil.isNullOrEmpty(msg)) {
            result.put("data",assemblyModel(args));
        }
        return result;
    }

    private static ConfigModel assemblyModel(String[] args) {
        ConfigModel model = new ConfigModel();
        model.setMsgCount(1);
        return model;
    }

    private static String checkPara(String[] args) {
        return "";
    }
}
