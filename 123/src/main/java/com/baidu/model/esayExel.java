package com.baidu.model;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName esayExel
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/14
 * @Version V1.0
 **/
public class esayExel {

    String PATH = "D:\\ideaCode\\123\\";

   private List<DemoData> data(){
       List<DemoData> list = new ArrayList<DemoData>();

       for(int i=0 ; i< 10 ; i++){
           DemoData demoData = new DemoData();
           demoData.setString("字符串"+i);
           demoData.setDate(new Date());
           demoData.setDoubleDate(0.56);
           list.add(demoData);
       }
       return list;
   }


   @Test
    public void SimpleWrite(){
        String fileName = PATH + "easyExel.xls";
       EasyExcel.write(fileName,DemoData.class).sheet("模板").doWrite(data());
    }

    // 最简单的读
    //重点是读取的逻辑
    //listener方法中要继承AnalysisEventListener<DemoData>方法  然后继承invoke方法     
    @Test
    public void simpleRead() {
        String fileName = PATH + "easyExel.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

}
