package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecificationCategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/22
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;




    @Override
    @Transactional
    public Result<Object> editCategoryById(CategoryEntity entity) {

        categoryMapper.updateByPrimaryKeySelective(entity);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<Object> addCategorybyquery(CategoryEntity entity) {
        //查询这条新增数据的pid 也就是父类的id
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(entity.getParentId());

        //判断父类是否为父节点
        if(categoryEntity.getIsParent() != 1){

            //判断新增时 将当前节点设置为父节点
            CategoryEntity updateCategoryEntity2 = new CategoryEntity();
            updateCategoryEntity2.setId(entity.getParentId());
            updateCategoryEntity2.setIsParent(1);
            categoryMapper.updateByPrimaryKeySelective(updateCategoryEntity2);
        }

        categoryMapper.insertSelective(entity);
        return this.setResultSuccess();
    }


    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);

        List<CategoryEntity> list = categoryMapper.select(categoryEntity);

        return this.setResultSuccess(list);
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryByBrandId(Integer brandId) {

        List<CategoryEntity> list = categoryMapper.getCategoryByBrandId(brandId);

        return this.setResultSuccess(list);
    }

    @Transactional
    @Override
    public Result<Object> deleteCategoryById(Integer id) {

        //判断前台传来的id是合法
        if(ObjectUtil.isNull(id) || id <= 0) return this.setResultError(HTTPStatus.OPERATION_ERROR,"id不合法");
        //判断数据是否存在
        CategoryEntity categoryEntity1 = categoryMapper.selectByPrimaryKey(id);
        if(ObjectUtil.isNull(categoryEntity1)) return this.setResultError(HTTPStatus.OPERATION_ERROR,"当前数据不存在");

        //判断 父节点下有几条 叶子节点
        //正常方法
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setParentId(categoryEntity1.getParentId());
//        List<CategoryEntity> select = categoryMapper.select(categoryEntity);

        //tk.mapper方法
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity1.getParentId());
        List<CategoryEntity> categoryEntities = categoryMapper.selectByExample(example);

        //查询tb_specification表中对应的品牌是否存在 ，存在的话不能被删除

        //如果叶子节点下的数据 <=1 时 就把当前父节点 修改为叶子节点
        if(categoryEntities.size() <= 1 ){

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setIsParent(0);
            categoryEntity.setId(categoryEntity1.getParentId());

            categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        }
        //删除当前的数据
        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
