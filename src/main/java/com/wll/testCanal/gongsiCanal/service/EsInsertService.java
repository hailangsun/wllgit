package com.wll.testCanal.gongsiCanal.service;

import com.alibaba.fastjson.JSONArray;
import com.wll.testCanal.gongsiCanal.enums.EsInsertEumns;

import java.util.List;

public interface EsInsertService {
    /**
     * 批量添加索引
     * @param jsonArray .
     * @param index .
     * @return .
     */
    EsInsertEumns insertEsBatch(JSONArray jsonArray, String index);
    /**
     * 批量删除索引
     * @param ids 需要删除的id
     * @param index 索引名
     * @return .
     */
    EsInsertEumns deleteEsBatch(List<String> ids, String index);

}
