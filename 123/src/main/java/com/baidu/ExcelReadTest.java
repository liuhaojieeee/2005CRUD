package com.baidu;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * @ClassName ExcelReadTest
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/2/10
 * @Version V1.0
 **/


public class ExcelReadTest {
    String PATH = "D:\\ideaCode\\123\\";

    @Test
    public void TestRead03() throws Exception{


        //通过读取流来获得数据
        FileInputStream inputStream = new FileInputStream(PATH+"123刘豪杰03.xls");

        //创建工作簿 进行所有的操作
        Workbook workbook = new HSSFWorkbook(inputStream);
        //通过索引获取页
        Sheet sheet = workbook.getSheetAt(0);

        //获取行
        Row row = sheet.getRow(0);
        //获取列
        Cell cell = row.getCell(0);

        System.out.println(cell.getStringCellValue());

        inputStream.close();


    }

    @Test
    public void TestRead07() throws Exception{


        //通过读取流来获得数据
        FileInputStream inputStream = new FileInputStream(PATH+"刘豪杰077.xlsx");

        //创建工作簿 进行所有的操作
        Workbook workbook = new XSSFWorkbook(inputStream);
        //通过索引获取页
        Sheet sheet = workbook.getSheetAt(0);

        //获取行
        Row row = sheet.getRow(0);
        //获取列
        Cell cell = row.getCell(1);

        System.out.println(cell.getNumericCellValue());

        inputStream.close();


    }

    @Test
    public void TestCellType() throws Exception{
        FileInputStream inputStream = new FileInputStream(PATH+"复杂数据结构.xls");

        Workbook workbook = new HSSFWorkbook(inputStream);


        //获取标题
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        if(row != null){
            int cells = row.getPhysicalNumberOfCells();
            for (int cellNum = 0; cellNum<cells ;cellNum++){
                Cell cell = row.getCell(cellNum);
                if(null != cell){
                    int type = cell.getCellType();
                    String value = cell.getStringCellValue();
                    System.out.print(value + " | ");
                }
            }
            System.out.println();
        }


        //获取内容  //获取所有的行
        int rows = sheet.getPhysicalNumberOfRows();

        for (int rowNum = 1 ; rowNum < rows; rowNum++){
            Row rowData = sheet.getRow(rowNum);
            if (rowData != null){
                 //获取所有的列
                int cells = row.getPhysicalNumberOfCells();
                for (int cellNum = 0 ;cellNum < cells; cellNum++){
                    System.out.print("["+(rowNum+1)+"-"+(cellNum+1)+"]");

                    Cell cell = rowData.getCell(cellNum);
                    //匹配数据类型
                    if(cell != null){
                        int cellType = cell.getCellType();
                        String cellValue = "";

                        switch (cellType){
                            case HSSFCell.CELL_TYPE_STRING://字符串
                                System.out.print("【STRING】");
                                cellValue = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN://布尔类型
                                System.out.print("【BOOLEAN】");
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_BLANK://空
                                System.out.print("【BLANK】");
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC://数字（日期 / 普通数字）
                                System.out.print("【NUMERIC】");
                                if(HSSFDateUtil.isCellDateFormatted(cell)){
                                    System.out.print("【日期】");
                                    Date dateCellValue = cell.getDateCellValue();
                                    cellValue = new DateTime(dateCellValue).toString("yyyy-MM-dd");
                                }else{
                                    //如果不是日期格式的话 。就防止数字过长
                                    System.out.print("【转换为字符串输出】");
                                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    cellValue = cell.toString();
                                }
                                break;
                            case HSSFCell.CELL_TYPE_ERROR://错误
                                System.out.print("【数据类型错误】");
                                break;
                        }
                        System.out.println(cellValue);
                    }

                }
            }
        }
        inputStream.close();

    }

    @Test
    public void Formula() throws Exception{

        FileInputStream inputStream = new FileInputStream(PATH + "公式.xls");

        Workbook workbook = new HSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        Row row = sheet.getRow(4);
        Cell cell = row.getCell(0);

        //拿到计算公式
        FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);

        int cellType = cell.getCellType();

        //输出单元格内容
        switch (cellType){
            case Cell.CELL_TYPE_FORMULA:
                String cellFormula = cell.getCellFormula();
                System.out.println(cellFormula);

                CellValue evaluate = formulaEvaluator.evaluate(cell);
                String cellValue = evaluate.formatAsString();
                System.out.println(cellValue);
        }

    }
}
