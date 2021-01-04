package com.baidu.shop.dao;

import io.swagger.annotations.Api;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @ClassName SpecParamDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/4
 * @Version V1.0
 **/
@Data
@Api("规格参数实体")
public class SpecParamDTO {

    @Id
    private Integer id;
    private Integer cid;
    private Integer groupId;
    private String name;
    @Column(name = "`numeric`")
    private Integer numeric;
    private String unit;
    private Integer generic;
    private Integer searching;
    private String segments;
}
