package com.wll.testCanal.gongsiCanal.common;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectorImpl implements Connector {
    @Value("${canal.zk.addr}")
    private String zkAddr;
    @Value("${dw.instance.name}")
    private String instanceName;

    @Override
    public CanalConnector connect() {
        CanalConnector canalConnector = CanalConnectors.newClusterConnector(zkAddr, instanceName, "", "");
        canalConnector.connect();
        canalConnector.subscribe("");
        return canalConnector;
    }
}