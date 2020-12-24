package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "商品分类接口")
public interface CategoryService {

    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value = "删除商品分类")
    @DeleteMapping(value = "category/delete")
    Result<Object> deleteCategoryById(Integer id);

    @ApiOperation(value = "修改")
    @PutMapping(value = "/category/edit")//声明哪个组下面的参数参加校验-->当前是校验Update组
    Result<Object> editCategoryById(@Validated(MingruiOperation.Update.class) @RequestBody CategoryEntity entity);

    @ApiOperation(value = "新增")
    @PostMapping(value = "/category/add")//声明哪个组下面的参数参加校验-->当前是校验Add组
    Result<Object> addCategorybyquery(@Validated(MingruiOperation.Add.class)  @RequestBody CategoryEntity entity);

}
