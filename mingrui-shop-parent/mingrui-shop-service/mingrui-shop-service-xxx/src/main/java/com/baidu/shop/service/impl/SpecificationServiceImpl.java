package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SpecificationDTO;
import com.baidu.shop.entity.SpecificationEntity;
import com.baidu.shop.mapper.SpecificationMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName SpecificationServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/4
 * @Version V1.0
 **/
@RestController
public class SpecificationServiceImpl extends BaseApiService implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;


    @Override
    public Result<JSONObject> deleteSpecification(Integer id) {
        specificationMapper.deleteByPrimaryKey(id);
        return setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> updateSpecification(SpecificationDTO specificationDTO) {
        specificationMapper.updateByPrimaryKeySelective(
                BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));
        return setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> addSpecification(SpecificationDTO specificationDTO) {

        specificationMapper.insertSelective(
                BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));

        return setResultSuccess();
    }

    @Override
    public Result<List<SpecificationEntity>> getSpecicationList(SpecificationDTO specificationDTO) {

        Example example = new Example(SpecificationEntity.class);

            example.createCriteria().andEqualTo("cid",
                    BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class).getCid());

        List<SpecificationEntity> list = specificationMapper.selectByExample(example);

        return setResultSuccess(list);
    }


}
