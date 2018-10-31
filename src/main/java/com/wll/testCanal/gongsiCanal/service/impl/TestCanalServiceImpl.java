package com.wll.testCanal.gongsiCanal.service.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Lists;
import com.wll.testCanal.gongsiCanal.AbstractDataModify;
import com.wll.testCanal.gongsiCanal.entity.TestEsDto;
import com.wll.testCanal.gongsiCanal.service.TestCanalService;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Service
public class TestCanalServiceImpl implements TestCanalService {
    @Override
    public List<TestEsDto> buildTestEsData(List<CanalEntry.RowData> rowDataList) throws Exception {
        List<TestEsDto> testEsDtoList = Lists.newArrayListWithExpectedSize(rowDataList.size());
        for (CanalEntry.RowData rowData : rowDataList) {
            //返回对应mysql数据库字段
            TestEsDto testEsDto = AbstractDataModify.transformAllRowDataToEntity(rowData, TestEsDto.class);
            testEsDtoList.add(testEsDto);
        }
        return testEsDtoList;
    }
}
