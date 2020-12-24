package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName CategoryEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/22
 * @Version V1.0
 **/
@ApiModel(value = "分类实体类")
@Data
@Table(name = "tb_category")
public class CategoryEntity {

    @Id
    @ApiModelProperty(name = "类目id",example = "1")
    private Integer id;

    @ApiModelProperty(name="类目名称")
    private String name;

    @ApiModelProperty(name="父类目id,顶级类目填0",example = "1")
    private Integer parentId;

    @ApiModelProperty(name="是否为父节点,0为否,1为是",example = "1")
    private Integer isParent;

    @ApiModelProperty(name="排序指数，越小越靠前",example = "1")
    private Integer sort;

}
