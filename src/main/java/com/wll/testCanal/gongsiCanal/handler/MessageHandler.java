package com.wll.testCanal.gongsiCanal.handler;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

public interface MessageHandler {

    void handleAndAck(List<CanalEntry.Entry> entries, CanalConnector connector, long batchId) throws Exception;
}