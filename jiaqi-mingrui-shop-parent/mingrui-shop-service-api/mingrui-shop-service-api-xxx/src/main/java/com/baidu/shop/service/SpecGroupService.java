package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SpecGroupDTO;
import com.baidu.shop.DTO.SpecParamDTO;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.validate.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格组接口")
public interface SpecGroupService {

    @GetMapping(value = "spec/list")
    @ApiOperation(value = "规格组查询")
    Result<List<SpecGroupEntity>> getSpecGroupByCid(SpecGroupDTO specGroupDTO);

    @PostMapping(value = "spec/save")
    @ApiOperation(value = "规格组新增")
    Result<JSONObject> addSpecGroup(@Validated(MingruiOperation.Add.class) @RequestBody SpecGroupDTO specGroupDTO);

    @PutMapping(value = "spec/save")
    @ApiOperation(value = "规格组修改")
    Result<JSONObject> editSpecGroup(@Validated(MingruiOperation.Update.class) @RequestBody SpecGroupDTO specGroupDTO);

    @DeleteMapping(value = "spec/delete/{id}")
    @ApiOperation(value = "规格组删除")
    Result<JSONObject> deleteSpecGroup(@PathVariable Integer id);


    @GetMapping(value = "param/list")
    @ApiOperation(value = "规格组参数查询")
    Result<List<SpecParamEntity>> getParamList(SpecParamDTO paramDTO);

    @PostMapping(value = "param/save")
    @ApiOperation(value = "规格组参数新增")
    Result<JSONObject> addParam(@Validated(MingruiOperation.Add.class) @RequestBody SpecParamDTO paramDTO);

    @PutMapping(value = "param/save")
    @ApiOperation(value = "规格组参数修改")
    Result<JSONObject> editParam(@Validated(MingruiOperation.Update.class) @RequestBody SpecParamDTO paramDTO);

    @DeleteMapping(value = "param/delete")
    @ApiOperation(value = "规格组参数删除")
    Result<JSONObject> deleteParam(Integer id);

}
