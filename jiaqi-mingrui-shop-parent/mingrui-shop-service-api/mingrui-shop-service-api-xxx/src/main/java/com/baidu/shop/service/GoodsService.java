package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SpuDTO;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpuEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api(tags = "商品列表")
public interface GoodsService {

    @ApiOperation(value = "商品查询")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuEntity>> getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value = "商品新增")
    @PostMapping(value = "goods/save")
    Result<JSONObject> addGoods(@RequestBody SpuDTO spuDTO);



}
