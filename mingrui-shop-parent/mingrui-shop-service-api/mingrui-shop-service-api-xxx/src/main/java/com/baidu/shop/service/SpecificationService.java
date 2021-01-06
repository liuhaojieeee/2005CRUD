package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SpecParamDTO;
import com.baidu.shop.dao.SpecificationDTO;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.entity.SpecificationEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("规格参数")
public interface SpecificationService {

    @ApiOperation("规格组查询")
    @GetMapping("spec/list/")
    Result<List<SpecificationEntity>> getSpecicationList(SpecificationDTO specificationDTO);

    @ApiOperation("规格组新增")
    @PostMapping("spec/save/")
    Result<JSONObject> addSpecification(@RequestBody SpecificationDTO specificationDTO);

    @ApiOperation("规格组修改")
    @PutMapping("spec/save/")
    Result<JSONObject> updateSpecification(@RequestBody SpecificationDTO specificationDTO);

    @ApiOperation("规格组删除")
    @DeleteMapping("spec/delete/{id}")
    Result<JSONObject> deleteSpecification(@PathVariable Integer id);


    @ApiOperation("规格组参数查询")
    @GetMapping("param/list/")
    Result<List<SpecParamEntity>> getParamList(SpecParamDTO specParamDTO);

    @ApiOperation("规格组参数新增")
    @PostMapping("param/save/")
    Result<JSONObject> getParamAdd(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation("规格组参数修改")
    @PutMapping("param/save/")
    Result<JSONObject> getParamEdit(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation("规格组参数删除")
    @DeleteMapping("param/delete")
    Result<JSONObject> getParamDelete(Integer id);

}
