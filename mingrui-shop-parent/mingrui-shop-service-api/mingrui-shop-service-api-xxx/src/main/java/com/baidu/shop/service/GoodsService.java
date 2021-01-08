package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SkuDTO;
import com.baidu.shop.dao.SpuDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品列表")
public interface GoodsService {

    @ApiOperation(value = "查询spu信息")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "goods/save")
    Result<JSONObject> saveGoods(@Validated(MingruiOperation.Add.class) @RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "goods/save")
    Result<JSONObject> editGoods(@Validated(MingruiOperation.Update.class) @RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "根据spuId查询detail数据")
    @GetMapping(value = "goods/getSpuDetailBySpuId")
    Result<JSONObject> getSpuDetailBySpuId(Integer spuId);

    @ApiOperation(value = "根据spuId查询detail数据")
    @GetMapping(value = "goods/getSkusBySpuId")
    Result<List<SkuDTO>> getSkusBySpuId(Integer spuId);

    @ApiOperation(value = "删除数据")
    @DeleteMapping(value = "/goods/delete")
    Result<List<SkuDTO>> deleteGoods(Integer spuId);

    @ApiOperation(value = "数据上下架")
    @PutMapping(value = "/edit/editSaleble")
    Result<JSONObject> editSaleble(@RequestBody SpuDTO spuDTO);



}
