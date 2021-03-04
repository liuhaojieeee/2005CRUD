package com.wd;

import java.lang.reflect.Method;

/**
 * @ClassName test
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/1
 * @Version V1.0
 **/
public class test {
    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.wd.initDemo");
        boolean annotation = clazz.isAnnotation();
        Method[] methods = clazz.getMethods();
        if(null != methods){
            for (Method method : methods ) {
                boolean present = method.isAnnotationPresent(initMethod.class);
                if(present){
                    method.invoke(clazz.getConstructor(null).newInstance(null),null);
                }
            }
        }
    }
}
