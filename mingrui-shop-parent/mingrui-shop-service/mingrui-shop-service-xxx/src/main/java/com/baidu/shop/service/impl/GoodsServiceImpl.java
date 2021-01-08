package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SkuDTO;
import com.baidu.shop.dao.SpuDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * @Date 2021/1/5
 * @Version V1.0
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {
    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<JSONObject> editSaleble(SpuDTO spuDTO) {

        System.out.println(spuDTO);

        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);

        goodsMapper.updateByPrimaryKeySelective(spuEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<SkuDTO>> deleteGoods(Integer spuId) {
        //删除4表
        goodsMapper.deleteByPrimaryKey(spuId);
        //删除spuDetail表
        spuDetailMapper.deleteByPrimaryKey(spuId);

        this.deleteSkuAndStock(spuId);

        return this.setResultSuccess();
    }

    private void deleteSkuAndStock(Integer spuId){
        //查询出来sku表中的数据
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);

        List<Long> collect = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());

        skuMapper.deleteByIdList(collect);
        stockMapper.deleteByIdList(collect);
    }

    private void addSkuAndStock(SpuDTO spuDTO,Date date,SpuEntity spuEntity){
        // 新增sku表和stock表
        spuDTO.getSkus().stream().forEach(skuDTO -> {
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuEntity.getId());
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock表
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }

    @Override
    public Result<JSONObject> editGoods(SpuDTO spuDTO) {
        //需要修改4张表 spu -> spuDetail -> sku -> stock
        //修改spu表
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        goodsMapper.updateByPrimaryKeySelective(spuEntity);
        //修改detail表
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailMapper.updateByPrimaryKeySelective(spuDetailEntity);

        //修改sku表和stock表
        //先通过spuId来查询到skus表中的数据
        this.deleteSkuAndStock(spuDTO.getId());

        //新增sku表和stock表
        this.addSkuAndStock(spuDTO,new Date(),spuEntity);

        return this.setResultSuccess();
    }

    @Override
    public Result<List<SkuDTO>> getSkusBySpuId(Integer spuId) {
        List<SkuEntity> list =  skuMapper.getSkusBySpuId(spuId);
        return this.setResultSuccess(list);
    }

    @Override
    public Result<JSONObject> getSpuDetailBySpuId(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    @Override
    @Transactional
    public Result<JSONObject> saveGoods(SpuDTO spuDTO) {

        final Date date = new Date();

        //新增spu表
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
            spuEntity.setSaleable(1);
            spuEntity.setValid(1);
            spuEntity.setCreateTime(date);
            spuEntity.setLastUpdateTime(date);
        goodsMapper.insertSelective(spuEntity);

        //新增spuDetail表
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
            spuDetailEntity.setSpuId(spuEntity.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);

        //新增sku表
        this.addSkuAndStock(spuDTO,new Date(),spuEntity);

        return this.setResultSuccess();
    }


    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {

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

        List<SpuEntity> entities = goodsMapper.selectByExample(example);
//第一种方式但是
//        List<SpuDTO> collect = entities.stream().map(spuEntity -> {
//            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
//
//            CategoryEntity categoryEntity1 = categoryMapper.selectByPrimaryKey(spuEntity.getCid1());
//            CategoryEntity categoryEntity2 = categoryMapper.selectByPrimaryKey(spuEntity.getCid2());
//            CategoryEntity categoryEntity3 = categoryMapper.selectByPrimaryKey(spuEntity.getCid3());
//            spuDTO1.setCategoryName(categoryEntity1.getName() + "/" + categoryEntity2.getName() + "/" + categoryEntity3.getName());
//
//            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
//            spuDTO1.setBrandName(brandEntity.getName());
//            return spuDTO1;
//        }).collect(Collectors.toList());

        //第二种方式
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
