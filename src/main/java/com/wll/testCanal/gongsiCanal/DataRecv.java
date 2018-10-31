package com.wll.testCanal.gongsiCanal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.wll.testCanal.gongsiCanal.common.Connector;
import com.wll.testCanal.gongsiCanal.handler.MessageHandler;
import com.wll.testCanal.gongsiCanal.service.MessageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Component("dataRecv")
public class DataRecv extends AbstractDataRecv {
    private static Logger logger = LoggerFactory.getLogger(DataRecv.class);

    @Autowired
    private Connector connector;

    @Value("${dw.rev.batchsize}")
    private int batchSize;
    @Autowired
    private MessageFilter filter;
    @Value("${dw.rev.interval}")
    private int messageInterval;
    @Autowired
    private MessageHandler handler;
    private boolean running = true;

    @Override
    public void start() {
        CanalConnector connect = connector.connect();
        while (running) {
            Message message = connect.getWithoutAck(batchSize);
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0) {//未获取到数据
                try {
                    Thread.sleep(messageInterval * 100);
                } catch (InterruptedException e) {
                    //do nothing;
                }
                continue;
            }
            logger.info("-----> receive message , batchId : {}", batchId);
            try {//获取数据
                // 消息过滤， 前移至此， 避免因接收到不需处理的消息而频繁开启/关闭下层事物
                List<CanalEntry.Entry> entries = message.getEntries();
                filter.filter(entries); //过滤事务的binlog
                // 消息处理
                if (entries != null && CollectionUtils.isNotEmpty(entries)) {
                    handler.handleAndAck(entries, connect, batchId);
                } else {
                    connect.ack(batchId);
                }
                logger.info("-----> message ack success, batchId : {}", batchId);
            } catch (Exception e) {
                // 失败回滚
//                connect.rollback(batchId);
                connect.ack(batchId);
                logger.error("-----> message handle error and ack rollback, batchId {}:", batchId, e);
//                stop(" Application stop, reason : message HANDLE error :" + e, connect);
            }
        }
    }

    private void stop(String alarmContent, CanalConnector connector) {
        running = false;
        if (connector != null) {
            connector.disconnect();
        }
//        simpleMailSenderUtil.sendMail("canal is error", alarmContent);
        System.out.println("发送邮件");
    }
}
