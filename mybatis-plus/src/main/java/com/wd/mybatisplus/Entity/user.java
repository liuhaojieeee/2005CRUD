package com.wd.mybatisplus.Entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wd.mybatisplus.enums.AgeEnums;
import com.wd.mybatisplus.enums.StatusEnums;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName user
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/1
 * @Version V1.0
 **/
@Data
public class user {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String title;
    private AgeEnums age;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    private Integer version;
    private StatusEnums status;
}
