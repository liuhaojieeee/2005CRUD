package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName specParamEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/26
 * @Version V1.0
 **/
@Data
@Table(name = "tb_spec_param")
public class SpecParamEntity {

    @Id
    private Integer id;
    private Integer cid;
    private Integer groupId;
    private String name;

    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;

}
