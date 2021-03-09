package com.baidu;

import com.baidu.Entity.Student;
import com.sun.org.apache.bcel.internal.classfile.Code;
import sun.management.resources.agent;
import sun.plugin2.os.windows.Windows;
import sun.security.provider.Sun;
import sun.tools.jar.resources.jar;
import sun.util.logging.resources.logging;

import java.util.Properties;

/**
 * @ClassName cla
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/8
 * @Version V1.0
 **/
public class cla {
    static {
        System.out.println("main");
    }
    public static void main(String[] args) {
        System.out.println(son.m = 300);
    }

}

class son extends parent{

    static {
        System.out.println("子类");
    }
}
class parent{
    static int m = 100;
    static {
        System.out.println( "父类");
    }
}