package com.baidu.shop.service;

import com.baidu.shop.DTO.BrandDTO;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Api(tags = "品牌接口")
public interface BrandService {

    @GetMapping(value="brand/list")
    @ApiOperation("品牌查询")
    Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO);


}
