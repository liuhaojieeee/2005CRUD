package com.mr.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitmqConnectionUtil
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/9
 * @Version V1.0
 **/

public class RabbitmqConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {
        //1.定义rabbit链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置超时时间
        factory.setConnectionTimeout(60000);
        //3.设置服务ip地址
        factory.setHost("127.0.0.1");
        //4.设置端口号
        factory.setPort(5672);
        //5.设置用户名,密码 ,虚拟主机.
        factory.setUsername("guest");
        factory.setPassword("guest");
        //6.根据工厂创建链接
        Connection connection = factory.newConnection();
        return connection;
    }



}
