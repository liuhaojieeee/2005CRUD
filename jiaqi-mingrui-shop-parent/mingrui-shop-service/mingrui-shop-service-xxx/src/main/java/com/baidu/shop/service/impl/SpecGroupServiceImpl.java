package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SpecGroupDTO;
import com.baidu.shop.DTO.SpecParamDTO;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.mapper.SpecParamMapper;
import com.baidu.shop.service.SpecGroupService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpecGroupServiceImpl
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/1/24
 * @Version V1.0
 **/
@RestController
public class SpecGroupServiceImpl extends BaseApiService implements SpecGroupService {


    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    @Transactional
    public Result<JSONObject> deleteParam(Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> editParam(SpecParamDTO paramDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(paramDTO, SpecParamEntity.class);
        specParamMapper.updateByPrimaryKeySelective(specParamEntity);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JSONObject> addParam(SpecParamDTO paramDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(paramDTO, SpecParamEntity.class);
        specParamMapper.insertSelective(specParamEntity);
        return this.setResultSuccess();

    }

    @Override
    public Result<List<SpecParamEntity>> getParamList(SpecParamDTO paramDTO) {

        SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(paramDTO, SpecParamEntity.class);
        Example example = new Example(SpecParamEntity.class);

        if(ObjectUtil.isNotNull(paramDTO.getGroupId())){
            example.createCriteria().andEqualTo("groupId",specParamEntity.getGroupId());
        }
        if(ObjectUtil.isNotNull(paramDTO.getCid())){
            example.createCriteria().andEqualTo("cid",specParamEntity.getCid());
        }
        List<SpecParamEntity> list = specParamMapper.selectByExample(example);

        return this.setResultSuccess(list);
    }





    //规格组CRUD
    @Override
    public Result<JSONObject> deleteSpecGroup(Integer id) {

        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        List<SpecParamEntity> list = specParamMapper.selectByExample(example);
        if(list.size() >= 1) return this.setResultError("当前节点下有参数，不能被删除");


        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> editSpecGroup(SpecGroupDTO specGroupDTO) {
        SpecGroupEntity groupEntity = BaiduBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class);
        specGroupMapper.updateByPrimaryKeySelective(groupEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> addSpecGroup(SpecGroupDTO specGroupDTO) {
        SpecGroupEntity groupEntity = BaiduBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class);
        specGroupMapper.insertSelective(groupEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<SpecGroupEntity>> getSpecGroupByCid(SpecGroupDTO specGroupDTO) {

        SpecGroupEntity groupEntity = BaiduBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class);

        Example example = new Example(SpecGroupEntity.class);
        example.createCriteria().andEqualTo("cid",groupEntity.getCid());

        List<SpecGroupEntity> list = specGroupMapper.selectByExample(example);



        return this.setResultSuccess(list);
    }


}
