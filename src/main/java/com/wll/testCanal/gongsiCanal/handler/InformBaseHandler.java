package com.wll.testCanal.gongsiCanal.handler;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InformBaseHandler extends AbstractHandler {

    @Override
    public void handleAndAck(List<CanalEntry.Entry> entries, CanalConnector connector, long batchId) throws Exception {
        // 转换批次下更新的
        transformDbToEntityAndPutES(entries);
        // ack保证原子性
        connector.ack(batchId);
    }

}
