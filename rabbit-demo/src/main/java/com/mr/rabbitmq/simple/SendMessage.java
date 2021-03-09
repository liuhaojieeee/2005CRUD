package com.mr.rabbitmq.simple;

import com.mr.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName SendMessage
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/9
 * @Version V1.0
 **/
public class SendMessage {

    //队列名称
    private final static String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.获取链接
        Connection connection = RabbitmqConnectionUtil.getConnection();
        //2.获取通道
        Channel channel = connection.createChannel();

        /*
        param1:队列名称
        param2: 是否持久化
        param3: 是否排外
        param4: 是否自动删除
        param5: 其他参数
        */
        //3.声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //设置要发送的消息
        String msg = "perseverance prevails";

        /*
        param1: 交换机名称
        param2: routingKey
        param3: 一些配置信息
        param4: 发送的消息
        */
        //发送消息
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println(" 消息发送 '" + msg + "' 到队列 success");
        channel.close();
        connection.close();
    }

}
