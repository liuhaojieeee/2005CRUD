package com.baidu.shop.dao;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecParamDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/4
 * @Version V1.0
 **/
    @Data
    @ApiModel("规格参数传递DTO")
    public class SpecParamDTO {

        @Id
        @ApiModelProperty(name = "主键",example = "1")
        @NotNull(message = "id不能为空",groups = {MingruiOperation.Update.class})
        private Integer id;

        @ApiModelProperty(name="cid",example = "1")
        @NotNull(message = "cid不能为空",groups = {MingruiOperation.Add.class})
        private Integer cid;

        @ApiModelProperty(name="groupId",example = "1")
        @NotNull(message = "groupId不能为空",groups = {MingruiOperation.Add.class})
        private Integer groupId;

        @ApiModelProperty(name="name不能为空")
        @NotEmpty(message = "name不能为空",groups = {MingruiOperation.Add.class})
        private String name;

        @ApiModelProperty(name="numeric",example = "1")
        @Column(name = "`numeric`")
        private Boolean numeric;

        @ApiModelProperty(name="unit")
        @NotEmpty(message = "unit不能为空",groups = {MingruiOperation.Add.class})
        private String unit;

        @ApiModelProperty(name="generic")
        private Boolean generic;

        @ApiModelProperty(name="searching")
        private Boolean searching;

        @ApiModelProperty(name = "segments")
        @NotEmpty(message = "segments不能为空",groups = {MingruiOperation.Add.class})
        private String segments;
    }
