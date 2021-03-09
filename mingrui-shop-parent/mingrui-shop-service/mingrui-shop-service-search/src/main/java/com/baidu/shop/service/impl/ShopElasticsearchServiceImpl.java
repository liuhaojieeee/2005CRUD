package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SkuDTO;
import com.baidu.shop.dao.SpecParamDTO;
import com.baidu.shop.dao.SpuDTO;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.response.GoodsResponse;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.service.ShopElasticsearchService;
import com.baidu.shop.utils.ESHighLightUtil;
import com.baidu.shop.utils.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.math.DoubleMath;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ShopElasticsearchServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/4
 * @Version V1.0
 **/
@RestController
public class ShopElasticsearchServiceImpl extends BaseApiService implements ShopElasticsearchService {

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private BrandFeign brandFeign;

    @Override
    public Result<List<GoodsDoc>> search(String search,Integer page,String filter) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        this.getNativeSearchQueryBuilder(nativeSearchQueryBuilder,search,page,filter);
        //结果查询
        SearchHits<GoodsDoc> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), GoodsDoc.class);
        List<GoodsDoc> goodsDocs = ESHighLightUtil.getHighlightList(searchHits.getSearchHits());
        //获取聚合数据
        Aggregations aggregations = searchHits.getAggregations();
//        Map<String,Long> totalMap = new HashMap<>();
//        totalMap.put("total",total);
//        totalMap.put("totalPage",totalPage);
        long total = searchHits.getTotalHits();
        long totalPage = Double.valueOf(Math.ceil(total / 10)).longValue();

        Map<Integer, List<CategoryEntity>> map = this.getCategoryListByBucket(aggregations);
        Integer hotCid = 0;
        List<CategoryEntity> categoryList = null;
        //entry遍历集合拿出数据
        for(Map.Entry<Integer,List<CategoryEntity>> entry : map.entrySet()){
            hotCid = entry.getKey();
            categoryList = entry.getValue();
        }

        return new GoodsResponse(total,totalPage,categoryList,
                this.getBrandListByBucket(aggregations),goodsDocs,getSpecMap(hotCid,search));
    }

    private Map<String, List<String>> getSpecMap(Integer hotCid,String search){
        SpecParamDTO paramDTO = new SpecParamDTO();
        paramDTO.setCid(hotCid);
        paramDTO.setSearching(true);
        Result<List<SpecParamEntity>> paramList = specificationFeign.getParamList(paramDTO);

        List<SpecParamEntity> specParamList  = paramList.getData();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(
                QueryBuilders.multiMatchQuery(search,"title","brandName","categoryName"));
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0,1));

        specParamList.stream().forEach(specParam ->{
            nativeSearchQueryBuilder
                    .addAggregation(AggregationBuilders.terms(specParam.getName())
                            .field("specs."+specParam.getName()+".keyword"));
        });


        SearchHits<GoodsDoc> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), GoodsDoc.class);
        Aggregations aggregations = searchHits.getAggregations();

        Map<String, List<String>> specMap = new HashMap<>();

        specParamList.stream().forEach(specParam ->{
            Terms aggregation = aggregations.get(specParam.getName());
            List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
                List<String> valueList = buckets.stream().map(bucket -> bucket.getKeyAsString())
                    .collect(Collectors.toList());

            specMap.put(specParam.getName(),valueList);
        });
        return specMap;
    }

    //得到NativeSearchQueryBuilder
    public NativeSearchQueryBuilder getNativeSearchQueryBuilder(NativeSearchQueryBuilder nativeSearchQueryBuilder,String search,Integer page,String filter){
        //多字段查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(search, "title", "brandName", "category"));

        //设置高亮
        nativeSearchQueryBuilder.withHighlightBuilder(ESHighLightUtil.getHighlightBuilder("title"));
        System.err.println(filter);
        //搜索过滤
        if(!StringUtils.isEmpty(filter) && filter.length() > 2){

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            Map<String, String> filterMap = JSONUtil.toMapValueString(filter);

            filterMap.forEach((key, value) -> {
                MatchQueryBuilder queryBuilder = null;
                if(key.equals("brandId") || key.equals("cid3")){
                    queryBuilder = QueryBuilders.matchQuery(key, value);
                }else{
                    queryBuilder = QueryBuilders.matchQuery("specs."+key+".keyword",value);
                }
                boolQueryBuilder.must(queryBuilder);
            });

            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        }

        //设置页数
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page,10));
        //搜索过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","skus"}, null));
        //聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("cate_agg").field("cid3"));//cid3是分类id
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brand_agg").field("brandId"));
        return nativeSearchQueryBuilder;
    }



    public List<BrandEntity> getBrandListByBucket(Aggregations aggregations ){

        Terms brand_agg = aggregations.get("brand_agg");

        List<? extends Terms.Bucket> brandBuckets = brand_agg.getBuckets();

        //要将List<Long>转换成 String类型的字符串并且用,拼接
        List<String> brandIdList = brandBuckets
                .stream().map(brandBucket -> brandBucket.getKeyAsNumber().longValue() + "")
                .collect(Collectors.toList());
        Result<List<BrandEntity>> brandResult = brandFeign.getBrandByIds(String.join(",", brandIdList));
        List<BrandEntity> brandList = brandResult.getData();
        return brandList;
    }


    //通过聚合得到分类List
    public Map<Integer, List<CategoryEntity>> getCategoryListByBucket(Aggregations aggregations){

        Terms cate_agg = aggregations.get("cate_agg");
        List<? extends Terms.Bucket> categoryBuckets = cate_agg.getBuckets();


        List<Long> docCount = Arrays.asList(0L);
        List<Integer> hotCid = Arrays.asList(0);

        //要将List<Long>转换成 String类型的字符串并且用,拼接
        List<String> cateIdList = categoryBuckets.stream().map(categoryBucket -> {

            if(categoryBucket.getDocCount() > docCount.get(0)){
                docCount.set(0,categoryBucket.getDocCount());
                hotCid.set(0,categoryBucket.getKeyAsNumber().intValue());
            }
            return categoryBucket.getKeyAsNumber().longValue() + "";
        }).collect(Collectors.toList());



        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCateByIds(String.join(",", cateIdList));
        List<CategoryEntity> categoryList  = categoryResult.getData();

        Map<Integer, List<CategoryEntity>> map = new HashMap<>();
        map.put(hotCid.get(0),categoryList);

        return map;
    }
















    @Override
    public Result<JSONObject> initGoodsEsData() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDoc.class);
        if (!indexOperations.exists()){
            indexOperations.create();
            indexOperations.createMapping();
        }
        List<GoodsDoc> goodsDocs = this.esGoodsInfo();
        elasticsearchRestTemplate.save(goodsDocs);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> clearGoodsEsData() {

        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(GoodsDoc.class);
        if (indexOps.exists()) {
            indexOps.delete();
        }
        return this.setResultSuccess();
    }


    public List<GoodsDoc> esGoodsInfo() {
        SpuDTO spuDTO = new SpuDTO();
//        spuDTO.setPage(1);
//        spuDTO.setRows(5);
        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(spuDTO);

        //mysql数据迁移到es  先填充spu
        if(spuInfo.getCode() == 200){
            //讲spu数据填充直es
            List<GoodsDoc> collect = spuInfo.getData().stream().map(spu -> {
                GoodsDoc goodsDoc = new GoodsDoc();
                goodsDoc.setId(spu.getId().longValue());
                goodsDoc.setBrandId(spu.getBrandId().longValue());
                goodsDoc.setBrandName(spu.getBrandName());
                goodsDoc.setCategoryName(spu.getCategoryName());
                goodsDoc.setCid1(spu.getCid1().longValue());
                goodsDoc.setCid2(spu.getCid2().longValue());
                goodsDoc.setCid3(spu.getCid3().longValue());
                goodsDoc.setCreateTime(spu.getCreateTime());
                goodsDoc.setTitle(spu.getTitle());
                goodsDoc.setSubTitle(spu.getSubTitle());
                //讲sku的属性进行填充 //price的属性也进行填充  通过list集合 并再skus便利的集合中添加
                this.getSkusData(spu.getId()).forEach((key, value) -> {
                    goodsDoc.setPrice(key);
                    goodsDoc.setSkus(JSONUtil.toJsonString(value));
                });
                //规格参数填充//获取规格参数  封装方法
                goodsDoc.setSpecs(this.getSpecParam(spu));
                return goodsDoc;
            }).collect(Collectors.toList());
            System.out.println(collect);
            return collect;
        }
        return null;
    }

    //将skus数据放入 goodsdoc中
    private Map<List<Long>,List<Map<String, Object>>> getSkusData(Integer SpuId){

        //通过map集合将price和skus集合返回
        Map<List<Long>,List<Map<String, Object>>> Map = new HashMap<>();

        Result<List<SkuDTO>> skus = goodsFeign.getSkusBySpuId(SpuId);
        if (skus.getCode() == 200) {

            List<Long> priceList = new ArrayList<>();
            List<Map<String, Object>> skuListMap = skus.getData().stream().map(sku -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", sku.getId());
                map.put("title", sku.getTitle());
                map.put("images", sku.getImages());
                map.put("price", sku.getPrice());
                priceList.add(sku.getPrice().longValue());
                return map;
            }).collect(Collectors.toList());

            Map.put(priceList,skuListMap);
        }
        return Map;
    }




    //将detail表中的数据放入 specParam表中的参数中
    private Map<String, Object> getSpecParam(SpuDTO spu){

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spu.getCid3());
        specParamDTO.setSearching(true);//直查询有查询条件的规格参数
        Result<List<SpecParamEntity>> specParamList = specificationFeign.getParamList(specParamDTO);
        if (specParamList.getCode() == 200) {
            List<SpecParamEntity> ParamList = specParamList.getData();
            //获取detail数据
            Result<SpuDetailEntity> spuDetailList = goodsFeign.getSpuDetailBySpuId(spu.getId());
            if (spuDetailList.getCode() == 200) {
                SpuDetailEntity spuDetailData = spuDetailList.getData();

                return this.getSpecParam(ParamList, spuDetailData);
            }
        }
        return null;
    }


    //将通用规格参数和特有规格参数   判断赋值
    private Map<String, Object> getSpecParam(List<SpecParamEntity> ParamList,SpuDetailEntity spuDetailData){

        ////将json字符串转换成map集合
        Map<String, String> genericSpec = JSONUtil.toMapValueString(spuDetailData.getGenericSpec());
        Map<String, List<String>> specialSpec = JSONUtil.toMapValueStrList(spuDetailData.getSpecialSpec());
        //需要查询两张表的数据 spec_param(规格参数名) spu_detail(规格参数值) --> 规格参数名 : 规格参数值
        Map<String, Object> SpecMap = new HashMap<String, Object>();

        ParamList.stream().forEach(specParam -> {
            if (specParam.getGeneric()) {
                if (specParam.getSearching() && !StringUtils.isEmpty(specParam.getSegments())) {
                    SpecMap.put(specParam.getName(),
                            chooseSegment(genericSpec.get(specParam.getId() + ""), specParam.getSegments(), specParam.getUnit()));
                } else {
                    SpecMap.put(specParam.getName(), genericSpec.get(specParam.getId() + ""));
                }
            } else {
                SpecMap.put(specParam.getName(), specialSpec.get(specParam.getId() + ""));
            }
        });
        return SpecMap;
    }








    private String chooseSegment(String value, String segments, String unit) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
// 保存数值段
        for (String segment : segments.split(",")) {
            String[] segs = segment.split("-");
// 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
// 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + unit + "以上";
                }else if(begin == 0){
                    result = segs[1] + unit + "以下";
                }else{
                    result = segment + unit;
                }
                break;
            }
        }
        return result;
    }

}



