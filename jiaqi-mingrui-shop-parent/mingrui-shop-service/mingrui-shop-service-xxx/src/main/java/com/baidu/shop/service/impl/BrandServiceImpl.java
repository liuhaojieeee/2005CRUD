package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.BrandDTO;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/22
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {


    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<JSONObject> deleteBrand(Integer id) {

        brandMapper.deleteByPrimaryKey(id);

        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);

        return this.setResultSuccess();
    }

    @Override
    public Result<List<BrandEntity>> getBrandById(Integer cid) {

        List<BrandEntity> list = categoryBrandMapper.getBrandbyId(cid);

        return this.setResultSuccess(list);
    }

    @Override
    @Transactional
    public Result<JSONObject> updateBrandList(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);

        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",brandEntity.getId());
        categoryBrandMapper.deleteByExample(example);

        this.addBatchBrandCategory(brandDTO.getCategories(),brandEntity.getId());

        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> addBrandList(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        //将name的首字母 直接赋值给letter
        brandEntity.setLetter(PinyinUtil
                .getUpperCase(String.valueOf(brandDTO.getName().toCharArray()[0]),false)
                .toCharArray()[0]);
        //新增letter
        brandMapper.insertSelective(brandEntity);

        String categories = brandDTO.getCategories();

        if(StringUtil.isEmpty(categories)) return this.setResultError("分类集合不能为空");

        this.addBatchBrandCategory(brandDTO.getCategories(),brandEntity.getId());



        return this.setResultSuccess();
    }

    @Override
    public Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO) {
        //分页
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        PageHelper.orderBy(brandDTO.getOrderBy());

        //将实体类的属性复制给BrandDTO
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //再搜索框做模糊查询
        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");

        List<BrandEntity> list = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> PageInfo = new PageInfo<>(list);

        return this.setResultSuccess(PageInfo);
    }


    private void addBatchBrandCategory(String categories,Integer id) {
        if (StringUtil.isEmpty(categories)) throw new RuntimeException();

        if (categories.contains(",")) {
            categoryBrandMapper.insertList(Arrays.asList(categories.split(","))
                    .stream()
                    .map(categoryIdStr -> {
                        return new CategoryBrandEntity(Integer.valueOf(categoryIdStr), id);
                    })
                    .collect(Collectors.toList()));
        } else {
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(id);
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }

}
