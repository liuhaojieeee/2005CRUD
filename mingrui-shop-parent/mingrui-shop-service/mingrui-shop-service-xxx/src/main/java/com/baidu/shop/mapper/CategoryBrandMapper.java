package com.baidu.shop.mapper;

import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface CategoryBrandMapper extends Mapper<CategoryBrandEntity>, InsertListMapper<CategoryBrandEntity> {

    @Select(value = "select * from tb_brand where id in (SELECT brand_id from tb_category_brand where category_id = #{cid})")
    List<BrandEntity> getBrandbyId(Integer cid);

}
