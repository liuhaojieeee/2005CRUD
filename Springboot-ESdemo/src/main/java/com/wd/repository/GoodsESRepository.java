package com.wd.repository;

import com.wd.Entity.GoodsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ClassName GoodsESRepository
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/3
 * @Version V1.0
 **/
public interface GoodsESRepository extends ElasticsearchRepository<GoodsEntity,Long> {

    List<GoodsEntity> findAllByTitle(String title);

    List<GoodsEntity> findByAndPriceBetween(Double start,Double end);

}
