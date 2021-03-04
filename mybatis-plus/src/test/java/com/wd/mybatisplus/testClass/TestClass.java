package com.wd.mybatisplus.testClass;

/**
 * @ClassName TestClass
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/3
 * @Version V1.0
 **/
public class TestClass {

    public static void main(String[] args) throws ClassNotFoundException {

        person person = new student();
        //通过对象获得
        Class c1 = person.getClass();
        System.out.println(c1.hashCode());

        //通过forname获得
        Class c2 = Class.forName("com.wd.mybatisplus.testClass.student");
        System.out.println(c2.hashCode());
        //通过类目获得
        Class c3 = student.class;
        System.out.println(c3.hashCode());

        Class<Integer> type = Integer.TYPE;
        System.out.println(type);

        System.out.println(c1.getSuperclass());

    }

}

class person{
    public String name;

    public person(String name) {
        this.name = name;
    }

    public person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "person{" +
                "name='" + name + '\'' +
                '}';
    }
}

class student extends person{
    public student() {
        this.name = "学生";
    }
}

class teacher extends person{
    public teacher(){
        this.name = "老师";
    }
}
