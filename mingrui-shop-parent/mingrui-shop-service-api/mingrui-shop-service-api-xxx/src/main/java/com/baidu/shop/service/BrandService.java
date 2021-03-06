package com.baidu.shop.service;


import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "品牌接口")
public interface BrandService {

    @GetMapping(value="brand/list")
    @ApiOperation("品牌查询")
    Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO);

    @GetMapping(value="brand/getBrandbyId")
    @ApiOperation("根据分类id查询品牌数据")
    Result<List<BrandEntity>> getBrandbyId(Integer cid);

    @PostMapping(value = "brand/save")
    @ApiOperation("品牌新增")
    Result<JSONObject> saveBrandList(@Validated(MingruiOperation.Add.class) @RequestBody BrandDTO brandDTO);

    @PutMapping(value = "brand/save")
    @ApiOperation("品牌修改")
    Result<JSONObject> editBrandList(@Validated(MingruiOperation.Update.class) @RequestBody BrandDTO brandDTO);

    @DeleteMapping(value="brand/delete")
    @ApiOperation("品牌删除")
    Result<JSONObject> deleteBrandCategoryById(Integer id);

    @ApiOperation(value="通过品牌id集合获取品牌")
    @GetMapping(value = "brand/getBrandByIds")
    Result<List<BrandEntity>> getBrandByIds(@RequestParam String brandIds);

}
