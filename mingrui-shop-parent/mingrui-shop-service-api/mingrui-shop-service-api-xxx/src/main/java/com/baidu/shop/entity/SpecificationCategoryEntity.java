package com.baidu.shop.entity;

import io.swagger.annotations.Api;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpecificationCategoryEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/4
 * @Version V1.0
 **/
@Data
@Table(name = "tb_specification")
public class SpecificationCategoryEntity {

    @Id
    private Integer categoryId;
    private Object specifications;

}
