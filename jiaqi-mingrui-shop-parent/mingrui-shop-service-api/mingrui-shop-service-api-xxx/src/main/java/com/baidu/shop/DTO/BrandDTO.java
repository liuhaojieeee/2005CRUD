package com.baidu.shop.DTO;

import com.baidu.shop.validate.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName BrandDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/22
 * @Version V1.0
 **/
@Data
@ApiModel(value = "品牌DTO")
public class BrandDTO extends BaseDTO {

    @ApiModelProperty(value = "品牌主键",example = "1")
    @NotNull(message = "主键不能为空", groups = {MingruiOperation.Update.class})
    private Integer id;
    @ApiModelProperty(value = "品牌名称")
    @NotEmpty(message = "名牌名称不能为空", groups =
            {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;
    @ApiModelProperty(value = "品牌图片")
    private String image;

    @ApiModelProperty(value = "品牌首字母")
    private Character letter;

    @ApiModelProperty(value = "品牌类别")
    @NotEmpty(message = "品牌类别不能为null",groups = {MingruiOperation.Add.class})
    private String categories;

}
