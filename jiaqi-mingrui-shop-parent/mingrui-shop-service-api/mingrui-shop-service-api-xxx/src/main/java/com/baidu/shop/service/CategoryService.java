package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @ClassName CateoryService
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/19
 * @Version V1.0
 **/
@Api(tags="商品分类接口")
public interface CategoryService {

    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value = "删除商品分类")
    @DeleteMapping(value = "category/delete")
    Result<Object> deleteCategoryById(Integer id);

    @ApiOperation(value = "修改商品分类")
    @PutMapping(value = "category/edit")
    Result<Object> editCategoryById(@RequestBody CategoryEntity entity);
}