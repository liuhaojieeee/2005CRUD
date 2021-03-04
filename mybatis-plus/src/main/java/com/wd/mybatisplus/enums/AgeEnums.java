package com.wd.mybatisplus.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum AgeEnums implements IEnum<Integer> {
    ONE(1,"一岁"),
    TWO(2,"二岁"),
    Three(3,"三岁");


    AgeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;


    @Override
    public Integer getValue() {
        return this.code;
    }
}
