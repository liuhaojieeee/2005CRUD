package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServices
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/19
 * @Version V1.0
 **/
@RestController
public class CategoryServices extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;


    @Override
    public Result<List<CategoryEntity>> getCategoryByBrandId(Integer brandId) {
        List<CategoryEntity> list = categoryMapper.getCategoryByBrandId(brandId);
        return this.setResultSuccess(list);
    }

    @Override
    @Transactional
    public Result<Object> addCategory(CategoryEntity entity) {

        //查询一下当前新增数据的父类数据
        CategoryEntity entity1 = categoryMapper.selectByPrimaryKey(entity.getParentId());

        //新增时往子类节点新增数据时  那么将当前的子类节点升级为父类节点
        if(entity1.getIsParent() != 1){

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(entity.getParentId());
            categoryEntity.setIsParent(1);
            categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        }

        categoryMapper.insertSelective(entity);

        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<Object> editCategoryById(CategoryEntity entity) {
        categoryMapper.updateByPrimaryKeySelective(entity);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<Object> deleteCategoryById(Integer id) {
        if(id == null && id <= 0) return this.setResultError("id不合法");

        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);

        if(categoryEntity == null) return this.setResultError("数据不存在");
        if(categoryEntity.getIsParent() == 1) return this.setResultError("父节点不能被删除");

        Example example = new Example(categoryEntity.getClass());
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> list = categoryMapper.selectByExample(example);

        if(list.size() <= 1){
            CategoryEntity categoryEntity1 = new CategoryEntity();
            categoryEntity1.setIsParent(0);
            categoryEntity1.setId(categoryEntity.getParentId());
            categoryMapper.updateByPrimaryKeySelective(categoryEntity1);
        }

        categoryMapper.deleteByPrimaryKey(id);


        return this.setResultSuccess();
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }


}
