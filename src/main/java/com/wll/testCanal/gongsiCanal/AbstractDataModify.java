package com.wll.testCanal.gongsiCanal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wll.testCanal.gongsiCanal.enums.EsInsertEumns;
import com.wll.testCanal.gongsiCanal.service.EsInsertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractDataModify {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDataModify.class);

    /**
     * 插入失败重试次数
     */
    private final static Integer TRY_COUNT = 3;

    @Autowired
    private EsInsertService esInsertService;

    /**
     * 数据处理
     * @modified By:
     * @param rowDataList 待处理list
     * @param countDownLatch 计数器
     * @param parentThread 父线程
     * @return:
     */
    public void dataHandle(List<CanalEntry.RowData> rowDataList, CountDownLatch countDownLatch, Thread parentThread){
        try {
            String handleName = this.getClass().getSimpleName();
            logger.info(">>>>>>>>>>>>>>>{}处理{}条数据 start", handleName, rowDataList.size());
            handle(rowDataList);
            logger.info(">>>>>>>>>>>>>>>{} end", handleName);
        }catch (Exception e){
            logger.error("{} 处理器处理失败：{}", this.getClass().getSimpleName(), e.getMessage(), e);
            parentThread.interrupt();
        } finally {
            countDownLatch.countDown();
        }
    }

    /**
     * 实际数据处理类
     * @modified By:
     * @param rowDataList
     * @return: void
     */
    public abstract void handle(List<CanalEntry.RowData> rowDataList) throws Exception;

    /**
     *RowData 转 entity
     * @param columnsList
     * @param c
     * @param <T>
     * @return
     * @throws IOException
     */
    protected  <T> T transformRowDataToEntity(List<CanalEntry.Column> columnsList, Class<T> c) throws IOException {
        Map map = Maps.newHashMap();
        for(CanalEntry.Column column : columnsList){
            map.put(column.getName(), column.getValue());
        }
        return JSON.toJavaObject((JSON) JSONObject.toJSON(map), c);
    }

    /**
     * 实体转换（全量）
     * @param rowData
     * @param c
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T transformAllRowDataToEntity(CanalEntry.RowData rowData, Class<T> c) throws IOException {
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        Map map = Maps.newHashMapWithExpectedSize(Math.max(beforeColumnsList.size(), afterColumnsList.size()));
        for(CanalEntry.Column column : beforeColumnsList){
            map.put(column.getName(), column.getValue());
        }
        for(CanalEntry.Column column : afterColumnsList){
            map.put(column.getName(), column.getValue());
        }
        return JSON.toJavaObject((JSON) JSONObject.toJSON(map), c);
    }

    /**
     * 将rows转为entitys
     * @modified By:
     * @param rowDataList
     * @param cls
     * @return: java.util.List<T>
     */
    public static <T> List<T> row2Entity(List<CanalEntry.RowData> rowDataList, Class<T> cls) throws IOException {
        List<T> entityList = Lists.newArrayListWithExpectedSize(rowDataList.size());
        for(CanalEntry.RowData rowData : rowDataList){
            entityList.add(transformAllRowDataToEntity(rowData, cls));
        }
        return entityList;
    }

    /**
     * 批量插入ES
     * @modified By:
     * @param indexName
     * @param jsonArray
     * @param tryCount
     * @return: void
     */
    protected void insertBatchForEs(String indexName, JSONArray jsonArray, Integer... tryCount){
        int tryTime = tryCount.length == 0 ? TRY_COUNT : tryCount[0];
        EsInsertEumns esInsertEumns;
        try{
            esInsertEumns = esInsertService.insertEsBatch(jsonArray, indexName);
            if(esInsertEumns != EsInsertEumns.SUCCESS){
                if(tryTime > 0){
                    insertBatchForEs(indexName, jsonArray, --tryTime);
                } else {
                    logger.error(">>>>数据插入ES({})失败",indexName);
                    throw new RuntimeException(">>>>数据插入ES(".concat(indexName).concat(")失败"));
                }
                //更新成功
            } else {
                logger.info(">>>>>>>>>>>>>>>>> ES更新成功，更新数量为：{}", jsonArray.size());
            }
        } catch (Exception e){
            if(tryTime > 0){
                insertBatchForEs(indexName, jsonArray, --tryTime);
            } else {
                logger.error(">>>>>数据插入ES({})失败", indexName, e);
                throw new RuntimeException(">>>>数据插入ES(".concat(indexName).concat(")失败"));
            }
        }
    }

}
