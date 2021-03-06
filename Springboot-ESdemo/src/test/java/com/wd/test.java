package com.wd;

import com.wd.Entity.GoodsEntity;
import com.wd.Utils.ESHighLightUtil;
import com.wd.repository.GoodsESRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName test
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/3
 * @Version V1.0
 **/
//让测试在Spring容器环境下执行
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ESdemoApplication.class})
public class test{


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsESRepository repository;



    //聚合度量  桶中桶
    @Test
    public void searchAggsMethod(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.addAggregation(AggregationBuilders.terms("agg_brand").field("brand")
                .subAggregation(AggregationBuilders.max("max_price").field("price")));



        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
        Aggregations aggregations = search.getAggregations();

        Terms agg_brand = aggregations.get("agg_brand");
        List<? extends Terms.Bucket> buckets = agg_brand.getBuckets();

        buckets.stream().forEach(bucket -> {

            Aggregations aggregations1 = bucket.getAggregations();
            Map<String, Aggregation> map = aggregations1.asMap();

               Max max_price = (Max) map.get("max_price");

            System.out.println(bucket.getKeyAsString()+":"+bucket.getDocCount()+"最大价格"+max_price.getValue());
        });


    }







    //聚合
    @Test
    public void searchAggs(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.addAggregation(AggregationBuilders.terms("agg_brand").field("brand"));

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
        Aggregations aggregations = search.getAggregations();
        Terms agg_brand = aggregations.get("agg_brand");
        List<? extends Terms.Bucket> buckets = agg_brand.getBuckets();

        buckets.stream().forEach(bucket -> {
            System.out.println(bucket.getKeyAsString()+"-"+bucket.getDocCount());
        });
    }





















    //自定义查询高亮
    @Test
    public void customSearchHighlight(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //单个查询
//        queryBuilder.withQuery(QueryBuilders.matchQuery("title","华为手机"));

        //布尔查询
        queryBuilder.withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title","华为手机"))
                .must(QueryBuilders.matchQuery("brand","华为"))
        );

        HighlightBuilder highlightBuilder = SetHighlight("title", "brand");

        //设置高亮
        queryBuilder.withHighlightBuilder(highlightBuilder);

        //重新设置title
        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
        List<SearchHit<GoodsEntity>> searchHits = search.getSearchHits();
        ESHighLightUtil.getHighlightList(searchHits);

        System.out.println(searchHits);
        //        List<SearchHit<GoodsEntity>> collect = search.stream().map(hit -> {
//            Map<String, List<String>> highlightFields = hit.getHighlightFields();
//
//            hit.getContent().setTitle(highlightFields.get("title").get(0));
//            hit.getContent().setBrand(highlightFields.get("brand").get(0));
//            System.out.println(hit);
//            return hit;
//        }).collect(Collectors.toList());
//        System.out.println(collect);
    }

    @Test
    public void tes(){
        String str = "title";
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        System.out.println(chars);
    }



    public static HighlightBuilder SetHighlight(String ...field){

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        Arrays.asList(field).forEach(s -> {
            highlightBuilder.field(s);
            highlightBuilder.preTags("<font style='color:red'>");
            highlightBuilder.postTags("</font>");
        });

        return highlightBuilder;
    }












    //自定义查询
    @Test
    public void customSearch() {


        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

//        //单条件查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","华为手机"));

//        //布尔查询
//        queryBuilder.withQuery(QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchQuery("title","华为手机"))
//                .mustNot(QueryBuilders.matchQuery("brand","三星"))
//                .must(QueryBuilders.rangeQuery("price").gte(4000).lt(5000))
//        );
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        //分页
//        queryBuilder.withPageable(PageRequest.of(1,2));


        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
        search.stream().forEach(searchHit -> {
            System.err.println(searchHit.getContent());
        });


    }















    @Test
    public void ConditionSearch(){

        List<GoodsEntity> title = repository.findAllByTitle("华为");
        System.out.println(title);
        System.out.println("=================");

        List<GoodsEntity> between = repository.findByAndPriceBetween(3000.00, 5000D);
        System.out.println(between);
    }



    @Test
    public void finAllData(){
        long count = repository.count();
        System.out.println(count);

        Iterable<GoodsEntity> all = repository.findAll();
        all.forEach(goods -> {
            System.out.println(goods);
        });
    }

    @Test
    public void deleteData(){
//        repository.deleteById(1L);
        GoodsEntity entity = new GoodsEntity();
        entity.setId(1L);
        repository.delete(entity);
    }






    @Test
    public void saveAllDate(){
        GoodsEntity entity = new GoodsEntity();
        entity.setId(1L);
        entity.setTitle("小米3");
        entity.setBrand("小米");
        entity.setCategory("手机");
        entity.setImages("xiaomi.jpg");
        entity.setPrice(1000D);

        repository.save(entity);
    }

    @Test//新增数据
    public void addGoods(){
        GoodsEntity entity = new GoodsEntity();
        entity.setId(2L);
        entity.setBrand("苹果");
        entity.setCategory("手机");
        entity.setImages("pingguo.jpg");
        entity.setPrice(5000D);
        entity.setTitle("iphone11手机");

        GoodsEntity entity2 = new GoodsEntity();
        entity2.setId(3L);
        entity2.setBrand("三星");
        entity2.setCategory("手机");
        entity2.setImages("sanxing.jpg");
        entity2.setPrice(3000D);
        entity2.setTitle("w2019手机");

        GoodsEntity entity3 = new GoodsEntity();
        entity3.setId(4L);
        entity3.setBrand("华为");
        entity3.setCategory("手机");
        entity3.setImages("huawei.jpg");
        entity3.setPrice(4000D);
        entity3.setTitle("华为mate30手机");

        repository.saveAll(Arrays.asList(entity,entity2,entity3));
        System.out.println("新增成功");
    }

    @Test
    public void UpdateDate(){
        GoodsEntity entity = new GoodsEntity();
        entity.setId(1L);
        entity.setTitle("8848");
        repository.save(entity);
    }






    //创建索引
    @Test
    public void createGoodsIndex(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("wangdie"));
        indexOperations.create();//创建索引
        System.out.println(indexOperations.exists()?"索引创建成功":"索引创建失败");
    }

    //创建映射
    @Test//不能用
    public void createGoodsMapper(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsEntity.class);
        indexOperations.createMapping();
        System.out.println("索引创建成功"+indexOperations.getMapping());
    }

    @Test
    public void deleteGoodsIndex(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsEntity.class);
        indexOperations.delete();
        System.out.println("删除索引成功");
    }





}
