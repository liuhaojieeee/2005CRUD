package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "id不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(name="类目名称")
    @NotEmpty(message = "分类名称不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;

    @ApiModelProperty(name="父类目id,顶级类目填0",example = "1")
    @NotNull(message = "pid不能为空",groups = {MingruiOperation.Add.class})
    private Integer parentId;

    @ApiModelProperty(name="是否为父节点,0为否,1为是",example = "1")
    @NotNull(message = "isParent不能为空",groups = {MingruiOperation.Add.class})
    private Integer isParent;

    @ApiModelProperty(name="排序指数，越小越靠前",example = "1")
    @NotNull(message = "sort不能为空",groups = {MingruiOperation.Add.class})
    private Integer sort;

}
