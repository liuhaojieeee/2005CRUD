package com.baidu.shop.entity;

import com.baidu.shop.validate.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName CategoryEntity
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/19
 * @Version V1.0
 **/
@ApiModel(value = "分类实体类")
@Data
@Table(name = "tb_category")
public class CategoryEntity {
    @Id
    @ApiModelProperty(value = "分类主键",example = "1")
    @NotNull(message = "id不为空" , groups = {MingruiOperation.Update.class})
    private Integer id;

    @NotEmpty(message = "分类名称不为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value = "分类名称")
    private String name;

    @NotNull(message = "父级分类不为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value = "父级分类",example = "1")
    private Integer parentId;

    @NotNull(message = "父级节点不为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value = "是否是父级节点",example = "1")
    private Integer isParent;

    @NotNull(message = "排序不为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value = "排序",example = "1")
    private Integer sort;

}
