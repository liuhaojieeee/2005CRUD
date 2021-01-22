package com.baidu.shop.service.impl;

import com.baidu.shop.DTO.BrandDTO;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/22
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {


    @Resource
    private BrandMapper brandMapper;

    @Override
    public Result<PageInfo<BrandEntity>> getBrandList(BrandDTO brandDTO) {
        //分页
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        PageHelper.orderBy(brandDTO.getOrderBy());

        //将实体类的属性复制给BrandDTO
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //再搜索框做模糊查询
        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");

        List<BrandEntity> list = brandMapper.selectByExample(example);
        PageInfo<BrandEntity> PageInfo = new PageInfo<>(list);

        return this.setResultSuccess(PageInfo);
    }


}
