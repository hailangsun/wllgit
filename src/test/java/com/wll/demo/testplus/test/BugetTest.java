package com.wll.demo.testplus.test;



import com.wll.demo.Excel4j.ExcelUtils;
import com.wll.demo.Excel4j.exceptions.Excel4JException;
import com.wll.demo.Excel4j.handler.ExcelHeader;
import com.wll.demo.Excel4j.handler.SheetTemplate;
import com.wll.demo.Excel4j.handler.SheetTemplateHandler;
import com.wll.demo.Excel4j.sheet.wrapper.NormalSheetWrapper;
import com.wll.demo.Excel4j.utils.Utils;
import com.wll.demo.testplus.entity.Budget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BugetTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // 基于模板、注解的多sheet导出
    @Test
    public void testObject2BatchSheet() throws Exception {

        List<NormalSheetWrapper> sheets = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Budget> list = new ArrayList<>();
            list.add(new Budget("1月", "10008.00", "","10004.00","6"));

            Map<String, String> data = new HashMap<>();
            data.put("title", "战争学院花名册");
            data.put("info", "学校统一花名册");
            sheets.add(new NormalSheetWrapper(i, list, data, Budget.class, false));
        }

//        String tempPath = "D:\\JProject\\Excel4J\\src\\test\\resources\\normal_batch_sheet_template.xlsx";//原
        String tempPath = "C:\\wanglili\\resourceCode\\EXCELEXPORT\\test.xlsx";
        FileOutputStream os = new FileOutputStream(new File("JK.xlsx"));
        // 基于模板导出Excel
//        ExcelUtils.getInstance().normalSheet2Excel(sheets, tempPath, "AA.xlsx");//原
        ExcelUtils.getInstance().normalSheet2Excel(sheets, tempPath, "C:\\wanglili\\resourceCode\\EXCELEXPORT\\CC.xlsx");
        ExcelUtils.getInstance().normalSheet2Excel(sheets, tempPath, os);
        os.close();
    }


    @Test
    public void temp(){

        long starTime = System.currentTimeMillis();


        String tempPath = "C:\\wanglili\\resourceCode\\EXCELEXPORT\\testcsv.csv";
        String targetPath = "C:\\wanglili\\resourceCode\\EXCELEXPORT\\CC.csv";
        //sheetIndex    指定导出Excel的sheet索引号(默认为0)
        int sheetIndex = 0;
        //扩展内容Map数据(具体就是key匹配替换模板#key内容,详情请查阅Excel模板定制方法)
        Map<String, String> extendMap = new HashMap<>();
        //是否写入标题
        boolean isWriteHeader = false;
        Class clazz = Budget.class;
        //测试模板
        jdbcTemplate.query("select * from luopan_project_huizong_new LIMIT 500000 OFFSET 0",new ResultSetExtractor() {

            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
//                ResultSetMetaData metaData = rs.getMetaData();
//                for(int i=0;i<metaData.getColumnCount();i++){
//                    System.out.println(metaData.getColumnName(i+1));
//                }

                try {
                //获取模板
                SheetTemplate template = SheetTemplateHandler.sheetTemplateBuilder(tempPath);
                SheetTemplateHandler.loadTemplate(template, sheetIndex);
                SheetTemplateHandler.extendData(template, extendMap);
                List<ExcelHeader> headers = Utils.getHeaderList(clazz);
                if (isWriteHeader) {
                    // 写标题
                    SheetTemplateHandler.createNewRow(template);
                    for (ExcelHeader header : headers) {
                        SheetTemplateHandler.createCell(template, header.getTitle(), null);
                    }
                }

                Map<String,Object> tempV = new HashMap<>();
                int rowIndex = 0;
                while (rs.next()){
                    rowIndex++;
                    tempV.put("month",rs.getInt("id"));
                    tempV.put("shouru_month",rs.getBigDecimal("value")+"");
                    tempV.put("maoli_month",rs.getBigDecimal("value1")+"");
                    tempV.put("shouru_week",rs.getString("huanbi"));

                    SheetTemplateHandler.createNewRow(template);
                    SheetTemplateHandler.insertSerial(template, null);

                    for (ExcelHeader header : headers) {
                        //SheetTemplateHandler.createCell(template, Utils.getProperty(object, header.getFiled(), header.getWriteConverter()), null);
                        SheetTemplateHandler.createCell(template,tempV.get(header.getFiled()), null);
                    }
//                    if (rowIndex % 100000 == 0) {
//                        System.out.println("rowIndex:"+rowIndex);
//                        template.write2File(targetPath);
//                        long endTime = System.currentTimeMillis();
//                        System.out.println("===============-rowIndex："+(endTime - starTime) / 1000 + "秒"+"===============");
//                    }
                }
                template.write2File(targetPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                long endTime = System.currentTimeMillis();
                System.out.println("===============-结束-时间："+(endTime - starTime) / 1000 + "秒"+"===============");
                return 0;
            }

        });
    }

}
