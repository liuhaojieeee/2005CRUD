package com.baidu.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AccEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/30
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccEntity {

    private Integer id;
    private String username;
    private String usersex;
    private Integer age;
}
