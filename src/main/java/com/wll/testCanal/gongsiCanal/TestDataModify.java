package com.wll.testCanal.gongsiCanal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wll.testCanal.gongsiCanal.entity.TestEsDto;
import com.wll.testCanal.gongsiCanal.mail.SimpleMailSenderUtil;
import com.wll.testCanal.gongsiCanal.service.TestCanalService;
import com.wll.testCanal.gongsiCanal.utils.FastJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * yuangong 是数据库表
 */
@Component("yuangong")
public class TestDataModify extends AbstractDataModify{

    @Value("${es.test_canal_index.name}")
    private String indexName;

    @Autowired
    private TestCanalService testCanalService;

    @Override
    public void handle(List<CanalEntry.RowData> rowDataList) throws Exception {
        List<TestEsDto> resultTestEsDtos = testCanalService.buildTestEsData(rowDataList);
        if(resultTestEsDtos.size() >0){
            System.out.println("==name:"+resultTestEsDtos.get(0).getName()+" ==age:"+resultTestEsDtos.get(0).getAge());
        }
        insertBatchForEs(indexName, FastJsonUtil.camel2LowerUnderScore(resultTestEsDtos));
    }
}
