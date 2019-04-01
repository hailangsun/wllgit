package com.wll.testMergeExcel;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询收款日报-票据明细，费项Dto
 * @date 2019/01/16
 * @author: wanglili
 * @return
 */
@Data
public class FeeTicketDetailDto implements Serializable {
    private String feeId;
    private String feeName;
    private String amount;
}
