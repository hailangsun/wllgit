package com.wll.testCanal.gongsiCanal.handler;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wll.testCanal.gongsiCanal.AbstractDataModify;
import com.wll.testCanal.gongsiCanal.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHandler implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

    private final static Integer TABLE_NUM = 10;

    /**
     * 处理类
     */
    @Autowired
    private Map<String, AbstractDataModify> handleMap = Maps.newHashMapWithExpectedSize(TABLE_NUM);

    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    /**
     * 转换canal提供的db数据为实体类形式
     */
    protected void transformDbToEntityAndPutES(List<CanalEntry.Entry> entries) throws Exception {
        Map<String, List<CanalEntry.RowData>> typeRowMap = Maps.newHashMapWithExpectedSize(handleMap.size());
        String tableName;
        // 消息解析 拆分不同类型的表数据，put map
        for (CanalEntry.Entry entry : entries) {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            // DDL操作,忽略
            if (rowChange.getIsDdl()) {
                continue;
            }
            logger.info("======> binlog[{}:{}] , talbe[{}.{}] , entryType:{}, eventType:{}",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    entry.getEntryType(), entry.getHeader().getEventType());
            tableName = entry.getHeader().getTableName().toLowerCase();
            //1. 通过表名，对数据更新操作进行分类
            List<CanalEntry.RowData> rowDataList = typeRowMap.get(tableName);
            if(rowDataList == null){
                rowDataList = Lists.newArrayList();
                typeRowMap.put(tableName, rowDataList);
            }
            rowDataList.addAll(rowChange.getRowDatasList());
        }
        Iterator<Map.Entry<String, List<CanalEntry.RowData>>> ite = typeRowMap.entrySet().iterator();
        CountDownLatch countDownLatch = new CountDownLatch(typeRowMap.size());
        Thread currThread = Thread.currentThread();
        while(ite.hasNext()){
            Map.Entry<String, List<CanalEntry.RowData>> entry = ite.next();
            AbstractDataModify snDataModify = handleMap.get(entry.getKey());
            threadPoolUtil.executeTask(() -> snDataModify.dataHandle(entry.getValue(), countDownLatch, currThread));
        }
        boolean exeResult;
        try {
            exeResult = countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(">>>>>>>>>>>>> 数据同步处理发生异常");
        }
        if(!exeResult){
            throw new RuntimeException(">>>>>>>>>>>>> 数据同步处理超时");
        }
    }

    public static void main(String[] args) {
        int a = 5+5/3;
        System.out.println(a);
    }

}
