package com.baidu.shop.mapper;

import com.baidu.shop.DTO.SkuDTO;
import com.baidu.shop.entity.SkuEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface SkuMapper extends Mapper<SkuEntity> , InsertListMapper<SkuEntity> , DeleteByIdListMapper<SkuEntity,Long> {

    @Select("select k.*,s.stock from tb_sku k, tb_stock s where k.id = s.sku_id and k.spu_id = #{spuId}")
    List<SkuDTO> getSkusBySpuId(Integer spuId);
}
