package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "页面静态化模板")
public interface TemplateService {

    @ApiOperation(value = "通过spuId创建一条模板")
    @GetMapping(value = "template/createStaticHTMLTemplate")
    Result<JSONObject> createStaticHTMLTemplate(Integer spuId);

    @ApiOperation(value = "创建所有模板")
    @GetMapping(value = "template/initStaticHTMLTemplate")
    Result<JSONObject> initStaticHTMLTemplate();

    @ApiOperation(value = "清除所有模板")
    @GetMapping(value = "template/clearStaticHTMLTemplate")
    Result<JSONObject> clearStaticHTMLTemplate();

    @ApiOperation(value = "通过spuId删除一条模板")
    @GetMapping(value = "template/deleteStaticHTMLTemplate")
    Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId);

}
