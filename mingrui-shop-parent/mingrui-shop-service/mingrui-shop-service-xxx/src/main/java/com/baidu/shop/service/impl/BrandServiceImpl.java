package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<List<BrandEntity>> getBrandByIds(String brandIds) {

        List<Integer> collect = Arrays.asList(brandIds.split(","))
                .stream().map(idStr -> Integer.parseInt(idStr)).collect(Collectors.toList());
        List<BrandEntity> list = brandMapper.selectByIdList(collect);

        return this.setResultSuccess(list);
    }

    @Override
    public Result<List<BrandEntity>> getBrandbyId(Integer cid) {

            List<BrandEntity> list = categoryBrandMapper.getBrandbyId(cid);

        return this.setResultSuccess(list);
    }

    @Override
    public Result<JSONObject> deleteBrandCategoryById(Integer id) {

        //品牌删除
        brandMapper.deleteByPrimaryKey(id);
        //删除品牌商品分类列表
        this.deleteBrandCategory(id);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> editBrandList(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);

//        Example example = new Example(CategoryBrandEntity.class);
//        example.createCriteria().andEqualTo("brandId",brandEntity.getId());
//        categoryBrandMapper.deleteByExample(example);
        this.deleteBrandCategory(brandEntity.getId());
        //批量新增中间表
        //先得到他的分类集合字符串
        this.addBatchBrandCategory(brandDTO.getCategories(),brandEntity.getId());

        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> saveBrandList(BrandDTO brandDTO) {



        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        //根据name的值根据pinyinUril工具类来判断name属性值 开头第一个字母 包括汉字的第一个字母
        //把name字符串转换为char类型的数组，要第一个下标为0的 ，因为工具类是string类型的，所以要根据String.valueOf转换为String类型的包装类
        //因为letter又是char类型的数据 所以要在后面添加一个tocharArray（）方法转换为char类型的
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);

        brandMapper.insertSelective(brandEntity);

        String categories = brandDTO.getCategories();
        if(StringUtils.isEmpty(categories)) return this.setResultError("分类集合不能为空");
//        List<CategoryBrandEntity> list = new ArrayList<>();

        if(categories.contains(",")){
            this.addBatchBrandCategory(brandDTO.getCategories(),brandEntity.getId());
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO) {
        //limit 作用? 限制查询
        //mybatis如何自定义分页插件 --> mybatis执行器
        if(ObjectUtil.isNotNull(brandDTO.getPage()))
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        if(!StringUtils.isEmpty(brandDTO.getSort()))
        PageHelper.orderBy(brandDTO.getOrderBy());
        //bean copy
        // BrandEntity brandEntity = new BrandEntity();
        // BeanUtils.copyProperties(brandDTO,brandEntity);
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(brandDTO.getName()))
        criteria.andLike("name","%"+brandEntity.getName()+"%");
        if(ObjectUtil.isNotNull(brandDTO.getId()))
        criteria.andEqualTo("id",brandEntity.getId());

        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> PageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(PageInfo);
    }





    //批量新增工具类
    private void addBatchBrandCategory(String categories,Integer id){
        if(StringUtils.isEmpty(categories)) throw  new RuntimeException();

        if(categories.contains(",")){
            categoryBrandMapper.insertList(Arrays.asList(categories.split(","))
                    .stream()
                    .map(categoryIdStr -> {
                        return new CategoryBrandEntity(Integer.valueOf(categoryIdStr),id);})
                    .collect(Collectors.toList()));
        }else{
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(id);
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }

        //        List<CategoryBrandEntity> list = new ArrayList<>();

//        if(categories.contains(",")){

//        String[] s = categories.split(",");
//            for(String s : split){
//                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
//                categoryBrandEntity.setBrandId(brandEntity.getId());
//                categoryBrandEntity.setCategoryId(Integer.valueOf(s));
//                list.add(categoryBrandEntity);
//            }
//            categoryBrandMapper.insertList(list);
//        }else{
//            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
//            categoryBrandEntity.setBrandId(brandEntity.getId());
//            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
//            categoryBrandMapper.insert(categoryBrandEntity);

        //批量新增2222
//            categoryBrandMapper.insertList(Arrays.asList(categories.split(","))
//                    .stream()
//                    .map(categoryIdStr -> {
//                        return new CategoryBrandEntity(Integer.valueOf(categoryIdStr),
//                                brandEntity.getId());})
//                    .collect(Collectors.toList()));
//          }
    }

    private void deleteBrandCategory(Integer id){
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);
    }



}
