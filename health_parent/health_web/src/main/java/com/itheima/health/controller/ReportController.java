package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetMealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;

    //会员数量统计
    @RequestMapping(path = "/getMemberReport")
    public Result getMemberReport(){

        try{
            //组织返回结果
            Map<String,Object> map = new HashMap<>();
            //先计算一年的月份
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);
            List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 12;i++){
                calendar.add(Calendar.MONTH,+1);
                Date date = calendar.getTime();
                String month = new SimpleDateFormat("yyyy-MM").format(date);
                monthList.add(month);
            }
            map.put("months",monthList);

            //然后再查询数据库，每个月所对应的增加会员数量
            List<Integer> memberCount = memberService.findMemberByRegTime(monthList);
            map.put("memberCount",memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //预约套餐占比统计
    @RequestMapping(path = "/getSetmealReport")
    public Result getSetmealReport(){
        Map<String,Object> map = new HashMap<>();
        try{
            //套餐的名称
            List<String> setmealNames = new ArrayList<>();
            map.put("setmealNames",setmealNames);
            //套餐的数量，这里面有名称
            List<Map<String,Object>> setmealCount = setMealService.findSetmealCount();
            //从setmealCount拿出套餐名称
            for (Map<String, Object> setmealMap : setmealCount) {
                String name = (String) setmealMap.get("name");
                setmealNames.add(name);
            }
            map.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    //查询会员数量按照性别来
    @RequestMapping(path = "/getMemberReportBySex")
    public Result getMemberReportBySex(){
        Map<String,Object> map = new HashMap<>();
        try{
            //会员性别
            List<String> memberSex = new ArrayList<>();
            map.put("memberSex",memberSex);
            //会员数量
            List<Map<String,Object>> memberCount = memberService.findMemberBySex();
            //拿出性别
            for (Map<String, Object> memberMap : memberCount) {
                String sex = (String) memberMap.get("name");
                memberSex.add(sex);
            }
            map.put("memberCount",memberCount);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //运营数据统计
    @RequestMapping(path = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try{
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    //导出运营数据报表
    @RequestMapping(path = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
           //远程调用报表服务获取报表数据
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //获得Excel模板文件绝对路径
            String path = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            sheet.getRow(2).getCell(5).setCellValue(reportDate);//日期

            sheet.getRow(4).getCell(5).setCellValue(todayNewMember); //今日新增会员数
            sheet.getRow(4).getCell(7).setCellValue(totalMember);   //总会员数

            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember); //本周新增会员数
            sheet.getRow(5).getCell(7).setCellValue(thisMonthNewMember);   //本月新增会员数

            sheet.getRow(7).getCell(5).setCellValue(todayOrderNumber); //今日预约数
            sheet.getRow(7).getCell(7).setCellValue(todayVisitsNumber);   //今日到诊数

            sheet.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber); //本周预约数
            sheet.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);   //本周到诊数

            sheet.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber); //本月预约数
            sheet.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);   //本月到诊数

            int rowNum = 12;
            //热门套餐
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name"); //套餐名称
                Long setmeal_count = (Long) map.get("setmeal_count"); //预约数量
                BigDecimal proportion = (BigDecimal) map.get("proportion"); //占比
                XSSFRow row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());

            }
            //同归输出流进行文件下载
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel"); //说明文件类型
            response.setHeader("content-Disposition","attachment;filename=report.xlsx"); //说明以附件的形式下载
            xssfWorkbook.write(outputStream);
            //关闭资源
            outputStream.close();
            xssfWorkbook.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    //统计会员数量(按年龄)
    @RequestMapping(path = "/getMemberReportByAge")
    public Result getMemberReportByAge(){
        Map<String,Object> map = new HashMap<>();
        try{
            //按照四个年龄段(0-18,18-30,30-45,45以上)
            //放年龄段
            List<String> ageList = new ArrayList<>();
            map.put("memberAge",ageList);
            //放年龄段所对应的会员数量
            List<Map<String,Object>> memberCount = memberService.findMemberByAge();
            for (Map<String, Object> memberMap : memberCount) {
                String name = (String) memberMap.get("name");
                ageList.add(name);
            }

            map.put("memberCount",memberCount);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

}
