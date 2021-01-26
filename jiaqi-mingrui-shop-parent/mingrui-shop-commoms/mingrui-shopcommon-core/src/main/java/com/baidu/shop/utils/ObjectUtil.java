package com.baidu.shop.utils;

import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ObjectUtil
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/26
 * @Version V1.0
 **/
@Configuration
public class ObjectUtil {
    public static Boolean isNull(Object obj) { return null == obj; }
    public static boolean isNotNull(Object obj) { return null != obj; }
}
