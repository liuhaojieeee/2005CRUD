package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.DTO.SpecGroupDTO;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.service.SpecGroupService;
import com.baidu.shop.utils.BaiduBeanUtil;
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

    @Override
    public Result<JSONObject> deleteSpecGroup(Integer id) {
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
