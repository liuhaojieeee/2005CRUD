package com.baidu.shop.dao;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName SpuDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/5
 * @Version V1.0
 **/
@ApiModel(value = "SPU数据传输DTO")
@Data
public class SpuDTO extends BaseDTO {

    @Id
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})//参数校验
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "子标题")
    @NotEmpty(message = "子标题不能为空",groups = {MingruiOperation.Add.class})
    private String subTitle;

    @ApiModelProperty(value = "一级类目",example = "1")
    @NotNull(message = "一级类目不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer cid1;

    @ApiModelProperty(value = "二级类目",example = "1")
    @NotNull(message = "二级类目不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer cid2;

    @ApiModelProperty(value = "三级类目",example = "1")
    @NotNull(message = "三级类目不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer cid3;

    @ApiModelProperty(value = "商品所属品牌id",example = "1")
    @NotNull(message = "商品所属品牌id不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer brandId;

    @ApiModelProperty(value = "是否上架",example = "1")
    @NotNull(message = "是否上架不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer saleable;

    @ApiModelProperty(value = "是否有效",example = "1")
    @NotNull(message = "是否有效不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Integer valid;

    @ApiModelProperty(value = "添加时间")
    @NotNull(message = "添加时间不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    @NotNull(message = "最后修改时间不能为空",groups = {MingruiOperation.Add.class})//参数校验
    private Date lastUpdateTime;

    private String brandName;
    private String categoryName;

}
