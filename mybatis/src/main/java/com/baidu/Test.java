package com.baidu;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/27
 * @Version V1.0
 **/
public class Test {


    {
        System.out.println("code execute");
    }

    stub stub1 = new stub();

    static {
        System.out.println("static code execute");
    }
    static stub stub = new stub();

    public static void main(String[] args) {
        System.out.println("main execute");

        Test s = new Test();
    }


}
