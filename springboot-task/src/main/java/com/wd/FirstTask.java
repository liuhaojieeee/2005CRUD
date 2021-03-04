package com.wd;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @ClassName FirstTask
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/26
 * @Version V1.0
 **/
@Configuration
@EnableScheduling
public class FirstTask {
    /**
     * 从0秒开始，每10秒执行一次
     */
    @Scheduled(cron="0/10 * * * * *")
    public void task1(){
        System.out.println("FirstTask.task1(),"+new Date());
    }
    /**
     * 间隔6秒执行一次
     */
    @Scheduled(fixedDelay=6000)
    public void task2(){
        System.out.println("FirstTask.task2(),"+new Date());
    }
}