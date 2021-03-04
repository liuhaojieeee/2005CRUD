package com.baidu.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName DemoData
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/14
 * @Version V1.0
 **/
@Data
public class DemoData {

    @ExcelProperty("字符串标题")
    private String string;

    @ExcelProperty("日期标题")
    private Date date;

    @ExcelProperty("数字标题")
    private Double doubleDate;

    //忽略这个字段
    @ExcelIgnore
    private String ignore;


}
