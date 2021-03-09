package com.baidu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.feign.BrandFeign;
import com.baidu.feign.CategoryFeign;
import com.baidu.feign.GoodsFeign;
import com.baidu.feign.SpecificationFeign;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.service.TemplateService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName TemplateServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/9
 * @Version V1.0
 **/
@RestController
public class TemplateServiceImpl extends BaseApiService implements TemplateService {

    private final Integer CREATE_STATIC_HTML = 1;
    private final Integer DELETE_STATIC_HTML = 2;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private TemplateEngine templateEngine;

    @Value(value = "${mrshop.static.html.path}")
    private String htmlPath;


    @Override
    public Result<JSONObject> createStaticHTMLTemplate(Integer spuId) {
        Map<String, Object> goodsInfo = this.getGoodsInfo(spuId);
        //创建上下文
        Context context = new Context();
        context.setVariables(goodsInfo);
        //创建要生成的文件
        File file = new File(htmlPath, spuId + ".html");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输出流
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file,"UTF-8");
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(ObjectUtil.isNotNull(printWriter)){
                printWriter.close();
            }
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> initStaticHTMLTemplate() {
        this.operationStaticHTML(CREATE_STATIC_HTML);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> clearStaticHTMLTemplate() {
        this.operationStaticHTML(DELETE_STATIC_HTML);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId) {
        File file = new File(htmlPath,spuId+".html");
        if(file.exists()){
            file.delete();
        }
        return this.setResultSuccess();
    }
    private Boolean operationStaticHTML(Integer operation){
        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(new SpuDTO());
        if(spuInfo.isSuccess()){
            spuInfo.getData().stream().forEach(spuDTO -> {
                if(operation == 1){
                    this.createStaticHTMLTemplate(spuDTO.getId());
                }else{
                    this.deleteStaticHTMLTemplate(spuDTO.getId());
                }
            });
        }
        return true;
    }








    private Map<String, Object> getGoodsInfo(Integer spuId) {
        Map<String, Object> goodsInfoMap = new HashMap<>();
        //获取spu数据
        SpuDTO spuData = this.getSpuInfo(spuId);
        goodsInfoMap.put("spuInfo", spuData);
        //spudetail数据
        goodsInfoMap.put("spuDetailInfo",this.getSpuDetail(spuData.getId()));
        //sku数据
        goodsInfoMap.put("skuInfo",this.getSkuInfo(spuData.getId()));
        //分类数据
        goodsInfoMap.put("category",this.getCategory(spuData.getCid1(),spuData.getCid2(),spuData.getCid3()));
        //品牌数据
        goodsInfoMap.put("brandInfo",this.getBrandInfo(spuData.getBrandId()));
        //规格组通用数据
        goodsInfoMap.put("specGroupAndParam",this.getSpecGroupAndParam(spuData.getCid3()));
        //规格组特有数据
        goodsInfoMap.put("specParamMap",this.getspecParamMap(spuData.getCid3()));

        return goodsInfoMap;
    }

    private SpuDTO getSpuInfo(Integer spuId){
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        Result<List<SpuDTO>> spuResultData = goodsFeign.getSpuInfo(spuDTO);
        SpuDTO spuData = null;
        if(spuResultData.isSuccess()){
            spuData = spuResultData.getData().get(0);
        }
        return spuData;
    }

    private SpuDetailEntity getSpuDetail(Integer spuId){
        SpuDetailEntity data = null;
        Result<SpuDetailEntity> spuDetailInfo = goodsFeign.getSpuDetailBySpuId(spuId);
        if (spuDetailInfo.isSuccess()) {
            data = spuDetailInfo.getData();
        }
        return data;
    }

    private List<SkuDTO> getSkuInfo(Integer spuId){
        List<SkuDTO> data = null;
        Result<List<SkuDTO>> skusInfo = goodsFeign.getSkusBySpuId(spuId);
        if (skusInfo.isSuccess()){
            data = skusInfo.getData();
        }
        return data;
    }

    private List<CategoryEntity> getCategory(Integer cid1,Integer cid2,Integer cid3){
        Result<List<CategoryEntity>> cateInfo = categoryFeign.getCateByIds(
                String.join(
                        ","
                        , Arrays.asList(
                                cid1 + "",
                                cid2 + "",
                                cid3 + "")
                )
        );
        List<CategoryEntity> data = null;
        if(cateInfo.isSuccess()){
            data = cateInfo.getData();
        }
        return data;
    }

    private BrandEntity getBrandInfo(Integer brandId){
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brandId);
        Result<PageInfo<BrandEntity>> brandInfo = brandFeign.getBrandList(brandDTO);
        BrandEntity brandEntity = null;
        if (brandInfo.isSuccess()){
            brandEntity = brandInfo.getData().getList().get(0);
        }
        return brandEntity;
    }

    private List<SpecificationDTO> getSpecGroupAndParam(Integer cid3){

        SpecificationDTO specGroupDTO = new SpecificationDTO();
        specGroupDTO.setCid(cid3);
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

        }

        return null;
    }


    private Map<Integer,String> getspecParamMap(Integer cid3){
        Map<Integer,String> specParamMap  = new HashMap<>();
        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(cid3);
        specParamDTO.setGeneric(false);
        Result<List<SpecParamEntity>> paramResult = specificationFeign.getParamList(specParamDTO);
        if(paramResult.isSuccess()){
            List<SpecParamEntity> paramData = paramResult.getData();
            paramData.stream().forEach(param-> specParamMap.put(param.getId(),param.getName()));
        }
        return specParamMap;
    }
}
