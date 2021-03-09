package com.mr.rabbitmq.work;

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

    //序列名称
    private final static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i=0 ;i < 100;i++){
            String msg = "perseverance prevails-"+i;

            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("send'"+ msg +"'success");
        }

        channel.close();
        connection.close();

    }
}
