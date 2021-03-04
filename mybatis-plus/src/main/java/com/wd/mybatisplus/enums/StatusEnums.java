package com.wd.mybatisplus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;

public enum StatusEnums {

    WORK(1,"工作"),
    REST(0,"休息");

    StatusEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @EnumValue
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
