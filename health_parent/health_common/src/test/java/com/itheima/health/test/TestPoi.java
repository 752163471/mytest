package com.itheima.health.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestPoi {


    @Test
    public void testPoiRead1() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\acer\\Desktop\\hello.xlsx"); //工作簿

        XSSFSheet sheet = workbook.getSheetAt(0); //工作表

        for (Row row : sheet) { //获取每一行

            for (Cell cell : row) {  //获取每一个单元格
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
    }

    @Test
    public void testPoiRead2() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\acer\\Desktop\\hello.xlsx");

        XSSFSheet sheet = workbook.getSheetAt(0);

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();

            for (int j = 0; j < lastCellNum;j++){
                XSSFCell cell = row.getCell(j);
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
    }

    @Test
    public void testPoiWrite() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("hello");

        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("性别");
        row.createCell(2).setCellValue("年龄");


        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("马内");
        row1.createCell(1).setCellValue("男");
        row1.createCell(2).setCellValue("27");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("阿诺德");
        row2.createCell(1).setCellValue("男");
        row2.createCell(2).setCellValue("20");

        OutputStream outputStream = new FileOutputStream("C:\\Users\\acer\\Desktop\\hello.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

    }
}
