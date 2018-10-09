package com.wll.demo.testplus.entity;


import com.wll.demo.Excel4j.annotation.ExcelField;

public class Budget {

    //月份
    @ExcelField(title = "月份", order = 1)
    private String month;

    //月归档收入
    @ExcelField(title = "归档收入", order = 2)
    private String shouru_month;
    //月归档毛利
    @ExcelField(title = "归档毛利", order = 3)
    private String maoli_month;

    //周归档收入
    @ExcelField(title = "归档收入", order = 4)
    private String shouru_week;

    //周归档毛利
    @ExcelField(title = "归档毛利", order = 5)
    private String maoli_week;

    public Budget(){

    }

    public Budget(String month, String shouru_month, String maoli_month, String shouru_week, String maoli_week) {
        this.month = month;
        this.shouru_month = shouru_month;
        this.maoli_month = maoli_month;
        this.shouru_week = shouru_week;
        this.maoli_week = maoli_week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShouru_month() {
        return shouru_month;
    }

    public void setShouru_month(String shouru_month) {
        this.shouru_month = shouru_month;
    }

    public String getMaoli_month() {
        return maoli_month;
    }

    public void setMaoli_month(String maoli_month) {
        this.maoli_month = maoli_month;
    }

    public String getShouru_week() {
        return shouru_week;
    }

    public void setShouru_week(String shouru_week) {
        this.shouru_week = shouru_week;
    }

    public String getMaoli_week() {
        return maoli_week;
    }

    public void setMaoli_week(String maoli_week) {
        this.maoli_week = maoli_week;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "month='" + month + '\'' +
                ", shouru_month='" + shouru_month + '\'' +
                ", maoli_month='" + maoli_month + '\'' +
                ", shouru_week='" + shouru_week + '\'' +
                ", maoli_week='" + maoli_week + '\'' +
                '}';
    }
}
