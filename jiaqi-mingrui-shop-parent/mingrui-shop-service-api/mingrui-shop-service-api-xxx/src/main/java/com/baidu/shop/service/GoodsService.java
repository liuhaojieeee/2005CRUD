package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SkuDTO;
import com.baidu.shop.DTO.SpuDTO;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpuEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品列表")
public interface GoodsService {

    @ApiOperation(value = "商品查询")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuEntity>> getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value = "商品新增")
    @PostMapping(value = "goods/save")
    Result<JSONObject> addGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "修改回显spu/spudetail")
    @GetMapping(value = "goods/getSpuDetailBySpuId")
    Result<JSONObject> getSpuDetailBySpuId(Integer spuId);

    @ApiOperation(value = "回显sku和stock表数据")
    @GetMapping(value = "goods/getSkusBySpuId")
    Result<List<SkuDTO>> getSkusBySpuId(Integer spuId);

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "goods/save")
    Result<JSONObject> editGood(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "删除商品")
    @DeleteMapping(value = "goods/delete")
    Result<JSONObject> deleteGoods(Integer spuId);


}
