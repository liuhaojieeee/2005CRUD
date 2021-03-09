package com.baidu.service.impl;

import com.baidu.feign.BrandFeign;
import com.baidu.feign.CategoryFeign;
import com.baidu.feign.GoodsFeign;
import com.baidu.feign.SpecificationFeign;
import com.baidu.service.PageService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName PageServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/8
 * @Version V1.0
 **/
//@Service
public class PageServiceImpl implements PageService {

//    @Autowired
    private GoodsFeign goodsFeign;

//    @Autowired
    private SpecificationFeign specificationFeign;

//    @Autowired
    private CategoryFeign categoryFeign;

//    @Autowired
    private BrandFeign brandFeign;

//    @Override
    public Map<String, Object> getGoodsInfo(Integer spuId) {
        Map<String, Object> goodsInfoMap = new HashMap<>();
        //获取spu数据
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        Result<List<SpuDTO>> spuResultData = goodsFeign.getSpuInfo(spuDTO);
        SpuDTO spuData = null;
        if(spuResultData.isSuccess()){
            spuData = spuResultData.getData().get(0);
            goodsInfoMap.put("spuInfo", spuData);
        }
        //spudetail数据
        Result<SpuDetailEntity> spuDetailInfo = goodsFeign.getSpuDetailBySpuId(spuId);
        if (spuDetailInfo.isSuccess()) {
            goodsInfoMap.put("spuDetailInfo",spuDetailInfo.getData());
        }
        //sku数据
        Result<List<SkuDTO>> skusInfo = goodsFeign.getSkusBySpuId(spuId);
        if (skusInfo.isSuccess()){
            goodsInfoMap.put("skuInfo",skusInfo.getData());
        }
        //分类数据

        Result<List<CategoryEntity>> cateInfo = categoryFeign.getCateByIds(
                String.join(
                        ","
                        , Arrays.asList(
                                spuData.getCid1() + "",
                                spuData.getCid2() + "",
                                spuData.getCid3() + "")
                )
        );

        if(cateInfo.isSuccess()){
            goodsInfoMap.put("category",cateInfo.getData());
        }
        //品牌数据
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(spuData.getBrandId());
        Result<PageInfo<BrandEntity>> brandInfo = brandFeign.getBrandList(brandDTO);
        if (brandInfo.isSuccess()){
            goodsInfoMap.put("brandInfo",brandInfo.getData().getList().get(0));
        }

        //规格组通用数据
        SpecificationDTO specGroupDTO = new SpecificationDTO();
        specGroupDTO.setCid(spuData.getCid3());
        Result<List<SpecificationEntity>> specGroupResult  = specificationFeign.getSpecicationList(specGroupDTO);
        if(specGroupResult.isSuccess()){
            List<SpecificationEntity> specGroupList = specGroupResult.getData();
            List<SpecificationDTO> SpecGroupAndParamList = specGroupList.stream().map(specGroup -> {
                SpecificationDTO specificationDTO = BaiduBeanUtil.copyProperties(specGroup, SpecificationDTO.class);

                SpecParamDTO specParamDTO = new SpecParamDTO();
                specParamDTO.setGroupId(specificationDTO.getId());
                specParamDTO.setGeneric(true);
                Result<List<SpecParamEntity>> paramList = specificationFeign.getParamList(specParamDTO);

                if (paramList.isSuccess()) {
                    specificationDTO.setSpecParamList(paramList.getData());
                }
                return specificationDTO;
            }).collect(Collectors.toList());
            goodsInfoMap.put("specGroupAndParam",SpecGroupAndParamList);
        }

        //规格组特有数据
        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spuData.getCid3());
        specParamDTO.setGeneric(false);
        Result<List<SpecParamEntity>> paramResult = specificationFeign.getParamList(specParamDTO);
        if(paramResult.isSuccess()){
            List<SpecParamEntity> paramData = paramResult.getData();
            Map<Integer,String> specParamMap  = new HashMap<>();
            paramData.stream().forEach(param-> specParamMap.put(param.getId(),param.getName()));
            goodsInfoMap.put("specParamMap",specParamMap);

        }

        return goodsInfoMap;
    }
}
