package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.BrandDTO;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.MingruiOperation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "品牌接口")
public interface BrandService {

    @GetMapping(value="brand/getBrandbyId")
    @ApiOperation("根据分类id查询品牌数据")
    Result<List<BrandEntity>> getBrandById(Integer cid);

    @GetMapping(value="brand/list")
    @ApiOperation("品牌查询")
    Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO);

    @PostMapping(value = "brand/save")
    @ApiOperation("品牌新增")
    Result<JSONObject> addBrandList(@Validated(MingruiOperation.Add.class) @RequestBody BrandDTO brandDTO);

    @PutMapping(value = "brand/save")
    @ApiOperation("品牌修改")
    Result<JSONObject> updateBrandList(@Validated(MingruiOperation.Update.class) @RequestBody BrandDTO brandDTO);

    @DeleteMapping(value = "brand/delete")
    @ApiOperation("品牌删除")
    Result<JSONObject> deleteBrand(Integer id);

}
