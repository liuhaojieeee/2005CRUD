package com.baidu.shop.service;


import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Table;
import java.util.List;

@Api(tags = "品牌接口")
public interface BrandService {

    @GetMapping(value="brand/list")
    @ApiOperation("品牌查询")
    Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO);

    @PostMapping(value = "brand/save")
    @ApiOperation("品牌新增")
    Result<JSONObject> saveBrandList(@RequestBody BrandDTO brandDTO);

    @PutMapping(value = "brand/save")
    @ApiOperation("品牌修改")
    Result<JSONObject> editBrandList(@RequestBody BrandDTO brandDTO);

    @DeleteMapping(value="brand/delete")
    @ApiOperation("品牌删除")
    Result<JSONObject> deleteBrandCategoryById(Integer id);

}
