package com.baidu.shop.dao;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpuDetailDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/7
 * @Version V1.0
 **/
@Data
@ApiModel(value = "spu(spuDetail)大字段数据传输类DTO")
@Slf4j
public class SpuDetailDTO {

    @ApiModelProperty(value = "spu主键",example = "1")
    @NotNull(message = "spu主键不能为null",groups = {MingruiOperation.Update.class})
    private Integer spuId;

    @ApiModelProperty(value = "商品描述信息")
    @NotEmpty(message = "商品描述信息不能为null",groups = {MingruiOperation.Add.class})
    private String description;

    @ApiModelProperty(value = "通用规格参数数据")
    @NotEmpty(message = "通用规格参数数据不能为null",groups = {MingruiOperation.Add.class})
    private String genericSpec;

    @ApiModelProperty(value = "特有规格参数")
    @NotEmpty(message = "特有规格参数不能null",groups = {MingruiOperation.Add.class})
    private String specialSpec;

    @ApiModelProperty(value = "包装清单")
    @NotEmpty(message = "包装清单null",groups = {MingruiOperation.Add.class})
    private String packingList;

    @ApiModelProperty(value = "售后服务")
    @NotEmpty(message = "售后服务null",groups = {MingruiOperation.Add.class})
    private String afterService;

}
