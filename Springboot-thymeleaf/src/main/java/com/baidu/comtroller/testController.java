package com.baidu.comtroller;

import com.baidu.Entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * @ClassName testController
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/7
 * @Version V1.0
 **/
@Controller
public class testController {

    @GetMapping("test")
    public String test(ModelMap map){
        map.put("name","tomcat");
        return "test";
    }

    @GetMapping("student")
    public String testStu(ModelMap map){
        Student student = new Student();
        student.setCode("001");
        student.setPass("64812121212");
        student.setAge(123);
        student.setLikeColor("<font color='pink'>粉色</font>");
        map.put("stu",student);
        return "student";
    }

    @GetMapping("list")
    public String list(ModelMap map){
        Student s1=new Student("001","111",18,"red");
        Student s2=new Student("002","222",19,"red");
        Student s3=new Student("003","333",16,"blue");
        Student s4=new Student("004","444",28,"blue");
        Student s5=new Student("005","555",68,"blue");
//转为List
        map.put("stuList", Arrays.asList(s1,s2,s3,s4,s5));
        return "list";
    }


    @GetMapping("model")
    public String model(Model model){

        ArrayList<Student> student = new ArrayList<>();

        for(int i=0  ; i<10 ; i++){
            student.add(new Student("a"+i,"b"+i,1+i,"c"+i));
        }
        model.addAttribute("student",student);
        return "model";
    }

}
