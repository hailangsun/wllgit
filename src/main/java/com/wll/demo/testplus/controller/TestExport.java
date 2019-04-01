//package com.wll.demo.testplus.controller;
//
//import com.wll.demo.Excel4j.ExcelUtils;
//import com.wll.demo.Excel4j.sheet.wrapper.NormalSheetWrapper;
//import com.wll.demo.testplus.entity.Budget;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.*;
//
//@Controller
//public class TestExport {
//
//    @RequestMapping("/TestExport")
//    public void TestExport(HttpServletResponse response){
//        try {
//            response.setContentType("application/xlsx;charset=utf-8");
//            response.setHeader("Content-disposition", "attachment;filename=" + "Test.xlsx");
//            List<NormalSheetWrapper> sheets = new ArrayList<>();
//            for (int i = 0; i < 2; i++) {
//                List<Budget> list = new ArrayList<>();
//                for(int j=0;j<2;j++){
//                    list.add(new Budget("1月", "10008.00", "","10004.00","6"));
//                }
//                Map<String, String> data = new HashMap<>();
//                data.put("title", "战争学院花名册1");
//                data.put("info", "学校统一花名册1");
//                sheets.add(new NormalSheetWrapper(i, list, data, Budget.class, false));
//            }
//            String tempPath = "C:\\wanglili\\resourceCode\\EXCELEXPORT\\test.xlsx";
//            OutputStream os = response.getOutputStream();
//            // 基于模板导出Excel
//            ExcelUtils.getInstance().normalSheet2Excel(sheets, tempPath, os);
//            os.close();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//}
