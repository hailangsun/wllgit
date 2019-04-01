package com.wll.testMergeExcel;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询收款日报-票据明细Dto
 * @date 2019/01/16
 * @author: wanglili
 * @return
 */
@Data
public class DailyTicketDetailDto implements Serializable {

    //收据号/退/付款票据号
    private String paidCode;
    //起始票号
    private String startPaidCode;
    //截止票号
    private String endPaidCode;
    //收据张数
    private String paidCodeNum;
    //断号收据
    private String breakPaidCode;

    //收款时间/退款/付款时间
    private String receDate;

    //结算方式id
    private String payModeId;
    private String payModeType;
    //结算方式
    private String payModeName;

    // 小区名称（必填项）
    private String regionId;
    // 小区名称
    private String regionName;

    //客户名称
    private String personName;

    // 房间id
    private String roomId;
    // 房间/车位编号
    private String roomSign;
    //收费项目
    private List<FeeTicketDetailDto> feeAmountDetail;
    //收费项目 动态表头
    private List<FeeTicketDetailDto> feeTitle;

    //收款id
    private String operatorId;
    //收款人name
    private String operatorName;

    /**
     * 是否作废
     */
    private String isCancel;

    //作废是否发生在本期
    private String isCurrentCancel;
    //作废时间
    private String updateTime;
    //支/汇票抬头
    private String noteTitle;
    //支/汇票号
    private String noteNo;
    //客户银行名称
    private String bankName;
    //客户银行账号
    private String bankAccount;

    private String feeId;
    private String feeName;
    //金额
    private String amount;
    //总金额
    private String totalAmount;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司名
     */
    private String companyName;

    //每个费项合计
    private String feeTotalAmount;

    //备注/付款原因
    private String memo;
}
