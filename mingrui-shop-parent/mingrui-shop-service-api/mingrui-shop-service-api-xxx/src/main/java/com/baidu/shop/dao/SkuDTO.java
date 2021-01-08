package com.baidu.shop.dao;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName SkuDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/7
 * @Version V1.0
 **/
@ApiModel(value = "SKU属性数据传输类DTO")
@Data
public class SkuDTO {

    @ApiModelProperty(value = "主键", example = "1")
    @NotNull(message = "主键不能为null",groups = {MingruiOperation.Update.class})
    private Long id;

    @ApiModelProperty(value = "spu主键", example = "1")
    @NotNull(message = "spu主键不能为null",groups = {MingruiOperation.Add.class})
    private Integer spuId;

    @ApiModelProperty(value = "商品标题")
    @NotEmpty(message = "商品标题不能为null",groups = {MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "商品图片")
    @NotEmpty(message = "商品图片不能为null",groups = {MingruiOperation.Add.class})
    private String images;

    @ApiModelProperty(value = "销售价格,单位为分", example = "1")
    @NotNull(message = "销售价格不能为null",groups = {MingruiOperation.Add.class})
    private Integer price;

    @ApiModelProperty(value = "特有规格属性在spu属性模板中的对应下标组合")
    private String indexes;

    @ApiModelProperty(value = "sku的特有规格参数键值对,json格式,反序列化时使用linkedHashMap,保证有序")
    private String ownSpec;


    //注意此处使用boolean值来接,在service中处理一下就可以了
    @ApiModelProperty(value = "是否有效，0无效，1有效", example = "1")
    private Boolean enable;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "库存")
    private Integer stock;


}
