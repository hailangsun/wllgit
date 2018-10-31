package com.wll.testCanal.gongsiCanal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wll.elasticsearchDemo.ESApiClientHelper;
import com.wll.testCanal.gongsiCanal.service.EsInsertService;
import com.wll.testCanal.gongsiCanal.enums.EsInsertEumns;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 批量插入es
 */
@Service
public class EsInsertServiceImpl implements EsInsertService {

    @Autowired
    private ESApiClientHelper helper;
    @Value("${es.test_canal_index.name}")
    private String canalIndex;

    //数据库id
    private static final String TEST_NAME = "id";


    /**
     * 先删除再插入
     *
     * @param jsonArray
     * @param index
     * @return
     */
    @Override
    public EsInsertEumns insertEsBatch(JSONArray jsonArray, String index) {
        if (jsonArray.isEmpty()) {
            //数据为空 直接返回成功
            return EsInsertEumns.SUCCESS;
        }
        String idName = "";
        if (canalIndex.equals(index)) {
            idName = TEST_NAME;
        }

        //bulk 批量执行
        BulkRequestBuilder bulkRequest = helper.getClient().prepareBulk();
        for (Object aJsonArray : jsonArray) {
            JSONObject jsonObject = (JSONObject) aJsonArray;
            if (!StringUtils.isEmpty(idName)) {
                String id = jsonObject.getString(idName);

                if (!StringUtils.isEmpty(id)) {
                    bulkRequest.add(helper.getClient().prepareDelete(index,"doc",id));
                    bulkRequest.add(helper.getClient().prepareIndex(index, "doc", id).setSource(jsonObject.toString()));
                }
            }
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            //如果有失败
            return EsInsertEumns.ERROR;
        }
        return EsInsertEumns.SUCCESS;
    }

    @Override
    public EsInsertEumns deleteEsBatch(List<String> ids, String index) {
        if (CollectionUtils.isEmpty(ids)) {
            //数据为空 直接返回成功
            return EsInsertEumns.SUCCESS;
        }
        //bulk 批量执行
        BulkRequestBuilder bulkRequest = helper.getClient().prepareBulk();
        for (String id : ids) {
            bulkRequest.add(helper.getClient().prepareDelete(index,"doc",id));
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            //如果有失败
            return EsInsertEumns.ERROR;

        }
        return EsInsertEumns.SUCCESS;
    }


}
