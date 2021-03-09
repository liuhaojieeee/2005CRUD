package com.mr.rabbitmq.simple;

import com.mr.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Receive
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/9
 * @Version V1.0
 **/
public class Receive {

    //队列名称
    private final static String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
//        获取rabbit链接
        Connection connection = RabbitmqConnectionUtil.getConnection();
//        获取通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义接收端
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                // body： 队列中参数信息
                String msg = new String(body);
                System.out.println("收到消息执行中" + msg + "!");
            }
        };

        /*
            param1 : 队列名称
            param2 : 是否自动确认消息
            param3 : 消费者
        */
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }
}
