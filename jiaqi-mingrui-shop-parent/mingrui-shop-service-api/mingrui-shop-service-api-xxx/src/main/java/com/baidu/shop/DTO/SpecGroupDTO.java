package com.baidu.shop.DTO;

import com.baidu.shop.validate.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecGroupDTO
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/24
 * @Version V1.0
 **/
@Data
@ApiModel(value = "规格组参数传递DTO")
public class SpecGroupDTO extends BaseDTO{

    @ApiModelProperty(value = "规格组主键",example = "1")
    @NotNull(message = "规格组主键不能为看空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "规格组cid",example = "1")
    @NotNull(message = "规格组cid不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名称")
    @NotEmpty(message = "规格组名称不能为空",groups = {MingruiOperation.Add.class})
    private String name;



}
