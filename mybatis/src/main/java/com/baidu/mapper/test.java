package com.baidu.mapper;

import com.baidu.Entity.AccEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.util.concurrent.*;

/**
 * @ClassName test
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/30
 * @Version V1.0
 **/
public class test {
    public static void main(String[] args) {
        /*
        创建线程池的7步
        1.创建线程链接数
        2.线程池最大连接数
        3.线程存活时间
        4.时间
        5.等待队列
        6.线程工厂
        7.拒绝策略
        * */

        ExecutorService executorService = new ThreadPoolExecutor(3, 5, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 9; i++) {
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName() + "===>办理业务");
            });
        }

    }
}
