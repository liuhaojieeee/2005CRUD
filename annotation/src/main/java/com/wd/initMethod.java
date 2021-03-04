package com.wd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName initDemo
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/1
 * @Version V1.0
 **/
@Target(ElementType.METHOD)//源注解 这个注解可以针对类  包 方法  参数。。。。
@Retention(RetentionPolicy.RUNTIME) //这个注解是针对编译时和运行时
public @interface initMethod {
}
