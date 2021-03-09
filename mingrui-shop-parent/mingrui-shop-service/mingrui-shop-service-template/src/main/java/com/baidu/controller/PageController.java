package com.baidu.controller;

import com.baidu.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName PageControlelr
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/8
 * @Version V1.0
 **/
@Controller
@RequestMapping(value = "item")
public class PageController {

    @Resource
    private PageService pageService;

    @GetMapping(value = "{spuId}.html")
    public String  test(@PathVariable(value = "spuId") Integer spuId, ModelMap modelMap){

        Map<String,Object> map = pageService.getGoodsInfo(spuId);
        modelMap.putAll(map);

        return "item";
    }
    @GetMapping(value = "qwe")
    public String  test1(){
        return "123";
    }

}
