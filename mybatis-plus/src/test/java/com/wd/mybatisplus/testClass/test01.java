package com.wd.mybatisplus.testClass;

import java.lang.annotation.ElementType;

/**
 * @ClassName test01
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/3
 * @Version V1.0
 **/
public class test01 {
    public static void main(String[] args) {

        Class c1 = Object.class;//类
        Class c2 = void.class;//void
        Class c3 = Comparable.class;  //接口
        Class c4 = Override.class;//注解
        Class c5 = ElementType.class; //枚举
        Class c6 = Integer.class;//基本数据类型
        Class c7 = String[].class;//数组
        Class c8 = int[][].class;//二维数组
        Class c9 = Class.class;//class
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
        System.out.println(c6);
        System.out.println(c7);
        System.out.println(c8);
        System.out.println(c9);
    }
}
