package com.wll.testMergeExcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OasExcel {


    public static void main(String[] ags){
        new OasExcel().testExcel();
    }

    /**
     * 生成列名
     */
    private SheetDefinition getSheetDefinition() {
        SheetDefinition sheetDefinition = new SheetDefinition();
        List<FieldDefinition> fieldDefinitionList1 = Lists.newArrayList();
        fieldDefinitionList1.add(new FieldDefinition("收据号","paidCode"));
        fieldDefinitionList1.add(new FieldDefinition("收款时间","receDate"));
        fieldDefinitionList1.add(new FieldDefinition("结算方式","payModeType"));
        fieldDefinitionList1.add(new FieldDefinition("小区名称","regionName"));
        fieldDefinitionList1.add(new FieldDefinition("客户名称","personName"));
        fieldDefinitionList1.add(new FieldDefinition("房间/车位编号","roomSign"));
        fieldDefinitionList1.add(new FieldDefinition("收费项目","feeAmountDetail"));
        sheetDefinition.setFieldList(fieldDefinitionList1);

        List<FieldDefinition> fieldDefinitionList2 = Lists.newArrayList();
        fieldDefinitionList2.add(new FieldDefinition("合计","operatorId"));
        fieldDefinitionList2.add(new FieldDefinition("收款人","operatorId"));
        fieldDefinitionList2.add(new FieldDefinition("是否作废","isCancel"));
        fieldDefinitionList2.add(new FieldDefinition("作废是否发生在本期","isCurrentCancel"));
        fieldDefinitionList2.add(new FieldDefinition("作废时间","updateTime"));
        fieldDefinitionList2.add(new FieldDefinition("支/汇票抬头","noteTitle"));
        fieldDefinitionList2.add(new FieldDefinition("支/汇票号","noteNo"));
        fieldDefinitionList2.add(new FieldDefinition("客户银行名称","bankName"));
        fieldDefinitionList2.add(new FieldDefinition("客户银行账号","bankAccount"));
        fieldDefinitionList2.add(new FieldDefinition("备注","bankAccount"));
        sheetDefinition.setFieldMergeList(fieldDefinitionList2);
        sheetDefinition.setDisplayName("计费报表-收款日报-票据明细");
        return sheetDefinition;
    }

    public void testExcel(){
        String ss = "[{\"allRegionName\":\"一个新的项目名称\",\"amount\":\"\",\"bankAccount\":\"\",\"bankName\":\"\",\"breakPaidCode\":\"\",\"companyId\":\"\",\"companyName\":\"\",\"endPaidCode\":\"SJ-0000001-201712111413031319d-190219-00002\",\"feeAmountDetail\":[{\"amount\":\"0.30\",\"feeId\":\"10004611\",\"feeName\":\"HCL_收入_角\"}],\"feeId\":\"\",\"feeName\":\"\",\"feeTitle\":[{\"amount\":\"\",\"feeId\":\"10004611\",\"feeName\":\"HCL_收入_角\"},{\"amount\":\"\",\"feeId\":\"10004727\",\"feeName\":\"HCL_收入类\"}],\"feeTotalAmount\":\"0.30\",\"isCancel\":\"否\",\"isCurrentCancel\":\"否\",\"memo\":\"无客户收款\",\"noteNo\":\"\",\"noteTitle\":\"\",\"operatorId\":\"\",\"operatorName\":\"黄春兰\",\"paidCode\":\"SJ-0000001-201712111413031319d-190219-00002\",\"paidCodeNum\":\"2\",\"payModeId\":\"\",\"payModeName\":\"千丁临停\",\"payModeType\":\"7\",\"personName\":\"\",\"receDate\":\"2019-02-19 17:03:05\",\"regionId\":\"201712111413031319d\",\"regionName\":\"一个新的项目名称\",\"roomId\":\"\",\"roomSign\":\"\",\"startPaidCode\":\"SJ-0000001-201712111413031319d-190219-00001\",\"totalAmount\":\"0.31\",\"updateTime\":\"\"},{\"allRegionName\":\"\",\"amount\":\"\",\"bankAccount\":\"\",\"bankName\":\"\",\"breakPaidCode\":\"\",\"companyId\":\"\",\"companyName\":\"\",\"endPaidCode\":\"\",\"feeAmountDetail\":[{\"amount\":\"0.01\",\"feeId\":\"10004727\",\"feeName\":\"HCL_收入类\"}],\"feeId\":\"\",\"feeName\":\"\",\"feeTitle\":[],\"feeTotalAmount\":\"0.01\",\"isCancel\":\"否\",\"isCurrentCancel\":\"否\",\"memo\":\"19号收费\",\"noteNo\":\"\",\"noteTitle\":\"\",\"operatorId\":\"\",\"operatorName\":\"黄春兰\",\"paidCode\":\"SJ-0000001-201712111413031319d-190219-00001\",\"paidCodeNum\":\"\",\"payModeId\":\"\",\"payModeName\":\"微信支付\",\"payModeType\":\"8\",\"personName\":\"纯纯业主\",\"receDate\":\"2019-02-19 16:23:49\",\"regionId\":\"201712111413031319d\",\"regionName\":\"一个新的项目名称\",\"roomId\":\"\",\"roomSign\":\"CL_A_15_01\",\"startPaidCode\":\"\",\"totalAmount\":\"\",\"updateTime\":\"\"}]";
        List<DailyTicketDetailDto> list = JSON.parseArray(ss,DailyTicketDetailDto.class);
        Workbook wb = new SXSSFWorkbook(200);
        SheetDefinition sheetDefinition = getSheetDefinition();
        Sheet sheet = wb.createSheet(sheetDefinition.getDisplayName());
        sheet.setDefaultColumnWidth(15);
        Map<String,String> totalFeeAmount = Maps.newHashMap();
        DailyTicketDetailDto firstDto = list.get(0);
        createTicketDetailTitle(sheet,firstDto);
          //费项合计列
        String totalAmount = "0";
        createTicketDetailMainTitle(sheet, sheetDefinition,firstDto,wb);
        createTicketDetailValue(wb,sheet,list,firstDto,totalFeeAmount,totalAmount);

        int rowIndex = sheet.getLastRowNum() + 1;

        //合计
        Row totalRow = sheet.createRow(rowIndex++);
        createCell(totalRow, 0, "合计");
        int colIndex = 5;
        for (FeeTicketDetailDto dto : firstDto.getFeeTitle()) {
            if(totalFeeAmount.get(dto.getFeeId()) != null){
                createCell(totalRow, colIndex += 1, totalFeeAmount.get(dto.getFeeId()));

            }else {
                createCell(totalRow, colIndex += 1, "");
            }
        }
        createCell(totalRow, colIndex += 1, totalAmount+"");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("D:/ttttt.xls");
            wb.write(fout);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void createTicketDetailTitle(Sheet sheet, DailyTicketDetailDto dto){
        // 生成标题行
        Row titleRow1 = sheet.createRow(0);
        Row titleRow2 = sheet.createRow(1);

        //收款总金额
        createCell(titleRow1, 0, "收款总金额");
        createCell(titleRow1, 1, dto.getTotalAmount());
        //公司
        createCell(titleRow1, 2, "公司");
        createCell(titleRow1, 3, dto.getCompanyName());
        //小区名称
        createCell(titleRow1, 4, "小区名称");
        createCell(titleRow1, 5, dto.getRegionName());
        //起始票号
        createCell(titleRow2, 0, "起始票号");
        createCell(titleRow2, 1, dto.getStartPaidCode());
        //截止票号
        createCell(titleRow2, 2, "截止票号");
        createCell(titleRow2, 3, dto.getEndPaidCode());
        //收据张数
        createCell(titleRow2, 4, "收据张数");
        createCell(titleRow2, 5, dto.getPaidCodeNum());
        //断号收据
        createCell(titleRow2, 6, "断号收据");
        createCell(titleRow2, 7, dto.getBreakPaidCode());

    }

    public void createTicketDetailMainTitle(Sheet sheet, SheetDefinition sheetDefinition,DailyTicketDetailDto dto,Workbook wb){
        List<FeeTicketDetailDto> feeTitle = dto.getFeeTitle();
        //合并
        int size    = sheet.getLastRowNum()+2;
        int length  = feeTitle.size();
        if(feeTitle == null){
            length = 0;
        }
        //收据号
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 0, 0));//起始行,结束行,起始列,结束列
        //收款时间
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 1, 1));
        //结算方式
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 2, 2));
        //小区名称
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 3, 3));
        //客户名称
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 4, 4));
        //房间/车位编号
        sheet.addMergedRegion(new CellRangeAddress(size, size +1, 5, 5));
        //收费项目
        if(feeTitle == null || feeTitle.size() == 0){
            sheet.addMergedRegion(new CellRangeAddress(size, size+1, 6, 6 ));
        }else {
            sheet.addMergedRegion(new CellRangeAddress(size, size, 6, 5 + length));
        }
        //合计
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 6 + length, 6 + length));
        //收款人
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 7 + length, 7 + length));
        //是否作废
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 8 + length, 8 + length));
        //作废是否发生在本期
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 9 + length, 9 + length));
        //作废时间
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 10 + length, 10 + length));
        //支/汇票抬头
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 11 + length, 11 + length));
        //支/汇票号
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 12 + length, 12 + length));
        //客户银行名称
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 13 + length, 13 + length));
        //客户银行账号
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 14 + length, 14 + length));
        //备注
        sheet.addMergedRegion(new CellRangeAddress(size, size + 1, 15 + length, 15 + length));

        //合并名称
        Row titleRow1 = sheet.createRow(size);
        for (short i = 0; i < sheetDefinition.getFieldList().size(); i++) {
            createMergeCell(wb,titleRow1, i, sheetDefinition.getFieldList().get(i).getName());
        }
        Row titleRow2 = sheet.createRow(size + 1);
        for (int i = 0; i < feeTitle.size(); i++) {
            createMergeCell(wb,titleRow2, i + 6, feeTitle.get(i).getFeeName());
        }
        for (short i = 0; i < sheetDefinition.getFieldMergeList().size(); i++) {
            createMergeCell(wb, titleRow1, i + 6 + length, sheetDefinition.getFieldMergeList().get(i).getName());
        }

    }

    public String createTicketDetailValue(Workbook wb, Sheet sheet, List<DailyTicketDetailDto> records, DailyTicketDetailDto firstDto,Map<String,String> totalFeeAmount,String totalAmount) {
        int rowIndex = sheet.getLastRowNum() + 1;
        List<FeeTicketDetailDto> feeTitle = firstDto.getFeeTitle();
        Map<String, String> feeAmountMap = Maps.newHashMap();

        for (int i = 0; i < records.size(); i++) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            //收据号
            createCell(row, colIndex, records.get(i).getPaidCode());
            //收款时间
            createCell(row, colIndex += 1, records.get(i).getReceDate());
            //结算方式
            createCell(row, colIndex += 1, records.get(i).getPayModeName());
            //小区名称
            createCell(row, colIndex += 1, records.get(i).getRegionName());
            //客户名称
            createCell(row, colIndex += 1, records.get(i).getPersonName());
            //房间/车位编号
            createCell(row, colIndex += 1, records.get(i).getRoomSign());
            //收费项目
            if (records.get(i).getFeeAmountDetail() != null && records.get(i).getFeeAmountDetail().size() > 0) {
                feeAmountMap = Maps.newHashMap();
                for (FeeTicketDetailDto dto : records.get(i).getFeeAmountDetail()) {
                    feeAmountMap.put(dto.getFeeId(), dto.getAmount());
                }
                //todo
                for (FeeTicketDetailDto dto : feeTitle) {
                    if(feeAmountMap.get(dto.getFeeId()) != null){
                        createCell(row, colIndex += 1, feeAmountMap.get(dto.getFeeId()));
                        //计算合计
                        if(totalFeeAmount.get(dto.getFeeId()) != null){
                            String amount = add(feeAmountMap.get(dto.getFeeId()),totalFeeAmount.get(dto.getFeeId()),2);
                            totalFeeAmount.put(dto.getFeeId(),amount);
                        }else {
                            totalFeeAmount.put(dto.getFeeId(),feeAmountMap.get(dto.getFeeId()));
                        }
                    }else {
                        createCell(row, colIndex += 1, "");
                    }
                }
            }
            //合计
            createCell(row, colIndex += 1, records.get(i).getFeeTotalAmount());
            //收款人
            createCell(row, colIndex += 1, records.get(i).getOperatorName());
            //是否作废
            createCell(row, colIndex += 1, records.get(i).getIsCancel());
            //作废是否发生在本期
            createCell(row, colIndex += 1, records.get(i).getIsCurrentCancel());
            //作废时间
            createCell(row, colIndex += 1, records.get(i).getUpdateTime());
            //支/汇票抬头
            createCell(row, colIndex += 1, records.get(i).getNoteTitle());
            //支/汇票号
            createCell(row, colIndex += 1, records.get(i).getNoteNo());
            //客户银行名称
            createCell(row, colIndex += 1, records.get(i).getBankName());
            //客户银行账号
            createCell(row, colIndex += 1, records.get(i).getBankAccount());
            //备注
            createCell(row, colIndex += 1, records.get(i).getMemo());

            totalAmount = add(totalAmount+"",records.get(i).getFeeTotalAmount(),2);
        }

        return totalAmount;
    }


    public String add(String s1, String s2, int scale) {
        BigDecimal d1 = toDec(s1);
        BigDecimal d2 = toDec(s2);
        BigDecimal result = d1.add(d2);
        result = result.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return result.toPlainString();
    }

    public BigDecimal toDec(String s) {
        try {
            if (s == null)
                s = "";
            if (s.length() == 0)
                return new BigDecimal(0);
            s = s.replaceAll(",", "");
            if (s.indexOf("E") >= 0) {
                // 如果含有E那么转换一下输出
                BigDecimal dc = new BigDecimal(s);
                s = dc.toPlainString();
            }
            return new BigDecimal(s);
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    /**
     * 创建单元格并设置样式
     *
     * @param row
     * @param column
     * @param value
     */
    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(String.valueOf(value));
    }

    /**
     * 收费率 - 创建单元格并设置样式
     */
    private void createMergeCell(Workbook wb, Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        //获取样式
        CellStyle cellStyle = createCellStyle(wb, true, true, false);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(String.valueOf(value));
    }

    /**
     * 合并单元格样式
     */
    private CellStyle createCellStyle(Workbook wb, boolean flag_AlignCenter, boolean flag_Bold, boolean flag_Border) {
        // 生成一个样式
        CellStyle cellStyle = wb.createCellStyle();
        //设置样式
        if (flag_AlignCenter) {
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//是否水平居中
        }
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中

        //是否加粗字体
        if (flag_Bold) {
            //创建字体
            Font font = wb.createFont();
            //粗体
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            //加载字体
            cellStyle.setFont(font);
        }

        //设置边框
        if (flag_Border) {
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        }

        return cellStyle;
    }
}
