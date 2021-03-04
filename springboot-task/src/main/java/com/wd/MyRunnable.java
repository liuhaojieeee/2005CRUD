package com.wd;

import java.util.Date;

/**
 * @ClassName MyRunnable
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/26
 * @Version V1.0
 * 创建一个线程，给定时任务调用
 **/
public class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("MyRunnable.run()，" + new Date());
    }

}
