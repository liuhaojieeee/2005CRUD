package com.baidu.shop.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName BrandEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/22
 * @Version V1.0
 **/
@Data
@Table(name = "tb_brand")
public class BrandEntity {

    @Id
    private Integer id;

    private String name;

    private String image;

    private Character letter;

}
