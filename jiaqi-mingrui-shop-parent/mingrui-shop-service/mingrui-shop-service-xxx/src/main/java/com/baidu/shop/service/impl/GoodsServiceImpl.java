package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SpuDTO;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/28
 * @Version V1.0
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {


    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper stockMapper;

    @Override
    @Transactional
    public Result<JSONObject> addGoods(SpuDTO spuDTO) {

        Date date = new Date();
        //先新增spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
            spuEntity.setSaleable(1);
            spuEntity.setValid(1);
            spuEntity.setCreateTime(date);
            spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);
        //新增spuDetail
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
            spuDetailEntity.setSpuId(spuDTO.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);

        //新增sku
            spuDTO.getSkus().stream().forEach(skuDTO -> {
                SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
                    skuEntity.setSpuId(spuEntity.getId());
                    skuEntity.setCreateTime(date);
                    skuEntity.setLastUpdateTime(date);
                skuMapper.insertSelective(skuEntity);
                //新增stock
                StockEntity stockEntity = new StockEntity();
                    stockEntity.setSkuId(skuEntity.getId());
                    stockEntity.setStock(skuDTO.getStock());
                stockMapper.insertSelective(stockEntity);
            });


        return this.setResultSuccess();
    }

    @Override
    public Result<List<SpuEntity>> getSpuInfo(SpuDTO spuDTO) {

        if(ObjectUtil.isNotNull(spuDTO.getPage()) && ObjectUtil.isNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());
        if(!StringUtil.isEmpty(spuDTO.getSort()) && !StringUtil.isEmpty(spuDTO.getOrder()))
            PageHelper.orderBy(spuDTO.getOrderBy());

        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if(ObjectUtil.isNotNull(spuDTO.getSaleable()) && spuDTO.getSaleable() < 2)
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        if(!StringUtil.isEmpty(spuDTO.getTitle()))
            criteria.andLike("title","%"+spuDTO.getTitle()+"%");

        List<SpuEntity> entities = spuMapper.selectByExample(example);

        List<SpuDTO> collect = entities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            //通过id查询 分类管理的数据
            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(
                    Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));


            //根据查询的数据，来获取到分类名称
            String collect1 = categoryEntities.stream().map(categoryEntity ->
                    categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(collect1);

            //根据brandId查询到品牌名称 赋值给spu中的BrandName
            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());


        PageInfo<SpuEntity> pageInfo = new PageInfo<>(entities);
        return this.setResult(HTTPStatus.OK,pageInfo.getTotal()+"",collect);
    }
}
