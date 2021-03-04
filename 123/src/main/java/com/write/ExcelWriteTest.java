package com.write;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @ClassName ExcelWriteTest
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/9
 * @Version V1.0
 **/
public class ExcelWriteTest {

    String PATH = "D:\\ideaCode\\123\\";

    @Test
    public void testWrite03() throws Exception {
        //1.创建工作簿
        Workbook workbook = new HSSFWorkbook();

        //2.创建sheet页
        Sheet sheet = workbook.createSheet("刘豪杰");

        //3,创建第一行
        Row row = sheet.createRow(0);
        //4.创建列
        Cell cell11 = row.createCell(0);
        cell11.setCellValue("03版本");

        Cell cell12 = row.createCell(1);
        cell12.setCellValue(666);

        //3,创建第二行
        Row row2 = sheet.createRow(1);
        //4.创建列
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("统计时间");

        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH-mm-ss");
        cell22.setCellValue(time);

        //生成表来输出(io 流形式)         03版本的话是xls结尾的
        FileOutputStream stream = new FileOutputStream(PATH + "刘豪杰03.xls");

        workbook.write(stream);

        //关闭流
        stream.close();

        System.out.println("03生成完毕");

    }


    @Test
    public void testWrite07() throws Exception {
        //1.创建工作簿
        Workbook workbook = new XSSFWorkbook();

        //2.创建sheet页
        Sheet sheet = workbook.createSheet("刘豪杰");

        //3,创建第一行
        Row row = sheet.createRow(0);
        //4.创建列
        Cell cell11 = row.createCell(0);
        cell11.setCellValue("07版本");

        Cell cell12 = row.createCell(1);
        cell12.setCellValue(666);

        //3,创建第二行
        Row row2 = sheet.createRow(1);
        //4.创建列
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("统计时间");

        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH-mm-ss");
        cell22.setCellValue(time);

        //生成表来输出(io 流形式)         03版本的话是xls结尾的
        FileOutputStream stream = new FileOutputStream(PATH + "刘豪杰077.xlsx");

        workbook.write(stream);

        //关闭流
        stream.close();

        System.out.println("07生成完毕");

    }

    //大文件输出03
    @Test
    public void testWrite03BigData() throws Exception {
        //1.创建工作簿
        Workbook workbook = new HSSFWorkbook();

        //2.创建sheet页
        Sheet sheet = workbook.createSheet("刘豪杰");
        long begin = System.currentTimeMillis();


        for (int rowNum=0 ; rowNum<65536 ;rowNum++){
            Row row = sheet.createRow(rowNum);
            for (int cellNum=0 ;cellNum<10; cellNum++){
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        //生成表来输出(io 流形式)         03版本的话是xls结尾的
        FileOutputStream stream = new FileOutputStream(PATH + "刘豪杰03bigData.xls");

        //写出
        workbook.write(stream);

        long end = System.currentTimeMillis();

        //关闭流
        stream.close();

        System.out.println((double) (end-begin)/1000);

    }

    //07
    @Test
    public void testWrite07BigData() throws Exception {
        //1.创建工作簿
        Workbook workbook = new XSSFWorkbook();

        //2.创建sheet页
        Sheet sheet = workbook.createSheet("刘豪杰");
        long begin = System.currentTimeMillis();


        for (int rowNum=0 ; rowNum<100000 ;rowNum++){
            Row row = sheet.createRow(rowNum);
            for (int cellNum=0 ;cellNum<10; cellNum++){
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        //生成表来输出(io 流形式)         03版本的话是xls结尾的
        FileOutputStream stream = new FileOutputStream(PATH + "刘豪杰07BigData.xlsx");

        //写出
        workbook.write(stream);

        long end = System.currentTimeMillis();

        //关闭流
        stream.close();

        System.out.println((double) (end-begin)/1000);

    }

    //加强版07  速度更快  但是会产生临时文件  加载完毕后可以关闭临时文件  dispose()方法关闭临时文件
    //耗费内存
    @Test
    public void testWrite07BigDatas() throws Exception {
        //1.创建工作簿
        Workbook workbook = new SXSSFWorkbook();

        //2.创建sheet页
        Sheet sheet = workbook.createSheet("刘豪杰");
        long begin = System.currentTimeMillis();


        for (int rowNum=0 ; rowNum<100000 ;rowNum++){
            Row row = sheet.createRow(rowNum);
            for (int cellNum=0 ;cellNum<10; cellNum++){
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        //生成表来输出(io 流形式)         03版本的话是xls结尾的
        FileOutputStream stream = new FileOutputStream(PATH + "刘豪杰07BigDatas.xlsx");

        //写出
        workbook.write(stream);

        //关闭加强版07的 产生的临时文件
        ((SXSSFWorkbook) workbook).dispose();

        //关闭流
        stream.close();
        long end = System.currentTimeMillis();
        System.out.println((double) (end-begin)/1000);

    }
}
