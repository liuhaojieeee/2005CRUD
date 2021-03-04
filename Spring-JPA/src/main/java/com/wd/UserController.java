package com.wd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/20
 * @Version V1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserJPA userJPA;

    /**
     * 查询所有数据
     *
     * @return
     */
    @RequestMapping("/list")
    public List<UserEntity> list() {
        return  userJPA.findAll();
    }

    @RequestMapping("/likeName")
    public List<UserEntity> nameLike(String name){
        return userJPA.findByNameLike("%"+name+"%");
    }

    //区间查询
    @RequestMapping("/between")
    public List<UserEntity> between(Integer start,Integer end){
        return userJPA.findByAndAgeBetween(start,end);
    }


    @RequestMapping("/save")
    public UserEntity save(UserEntity entity) {
        return  userJPA.save(entity);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public List<UserEntity> delete(Long id) {
        userJPA.deleteById(id);
        return userJPA.findAll();
    }
}
