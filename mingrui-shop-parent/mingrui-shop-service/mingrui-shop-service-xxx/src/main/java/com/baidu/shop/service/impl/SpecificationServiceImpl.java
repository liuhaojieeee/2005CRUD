package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dao.SpecParamDTO;
import com.baidu.shop.dao.SpecificationDTO;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.entity.SpecificationEntity;
import com.baidu.shop.mapper.SpecParamMapper;
import com.baidu.shop.mapper.SpecificationMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

    @Resource
    private SpecificationMapper specificationMapper;

    @Resource
    private SpecParamMapper specParamMapper;



    //规格组参数CRUD
    @Override
    @Transactional
    public Result<JSONObject> getParamDelete(Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> getParamAdd(SpecParamDTO specParamDTO) {
        specParamMapper.insertSelective(
                BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> getParamEdit(SpecParamDTO specParamDTO) {
        specParamMapper.updateByPrimaryKeySelective(
                BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    @Override
    public Result<List<SpecParamEntity>> getParamList(SpecParamDTO specParamDTO) {

        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(specParamDTO, SpecParamEntity.class);
        Example example = new Example(SpecParamEntity.class);
        Example.Criteria criteria = example.createCriteria();


        if(ObjectUtil.isNotNull(specParamDTO.getGroupId()))
            criteria.andEqualTo("groupId", specParamEntity.getGroupId());

        if(ObjectUtil.isNotNull(specParamDTO.getCid()))
            criteria.andEqualTo("cid",specParamEntity.getCid());

        List<SpecParamEntity> paramEntities = specParamMapper.selectByExample(example);

        return this.setResultSuccess(paramEntities);
    }




    //规格组CRUD
    @Override
    public Result<JSONObject> deleteSpecification(Integer id) {
        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        List<SpecParamEntity> entities = specParamMapper.selectByExample(example);
        if(entities.size() >= 1) return this.setResultError("规格组内存在参数，不能删");


        specificationMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> updateSpecification(SpecificationDTO specificationDTO) {
        specificationMapper.updateByPrimaryKeySelective(
                BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> addSpecification(SpecificationDTO specificationDTO) {

        specificationMapper.insertSelective(
                BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));

        return this.setResultSuccess();
    }

    @Override
    public Result<List<SpecificationEntity>> getSpecicationList(SpecificationDTO specificationDTO) {

        Example example = new Example(SpecificationEntity.class);
        Integer cid = BaiduBeanUtil.copyProperties(specificationDTO, SpecificationEntity.class).getCid();
        example.createCriteria().andEqualTo("cid",cid);

        List<SpecificationEntity> list = specificationMapper.selectByExample(example);

        return this.setResultSuccess(list);
    }


}
