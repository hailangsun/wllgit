package com.wll.testCanal.gongsiCanal.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wll.testCanal.gongsiCanal.entity.TestEsDto;

import java.util.List;

public interface TestCanalService {
    List<TestEsDto> buildTestEsData(List<CanalEntry.RowData> rowDataList) throws Exception;
}
