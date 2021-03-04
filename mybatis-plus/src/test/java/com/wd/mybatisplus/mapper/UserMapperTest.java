package com.wd.mybatisplus.mapper;

import com.wd.mybatisplus.Entity.user;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

   @Test
    void test(){
        mapper.selectList(null).forEach(System.out::println);
    }

    @Test
    void save(){
       user user = new user();
       user.setTitle("猪瘟童");
       mapper.insert(user);
        System.out.println(user);
    }

    @Test
    void update(){
       //sql-- update version = 3 = version = 2
        user user = mapper.selectById(7);
        user.setTitle("一号");
        //sql-- update version = 3 = version = 2
        user user2 = mapper.selectById(7);
        user2.setTitle("二号");

        mapper.updateById(user2);
        mapper.updateById(user);

    }

}