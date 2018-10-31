package com.wll.testCanal.gongsiCanal.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.NameFilter;
import com.google.common.base.CaseFormat;

import java.util.List;

public class FastJsonUtil {

    /**
     * list转为JSONArray（camel转为lower_underscore）
     * @modified By:
     * @param list
     * @return: com.alibaba.fastjson.JSONArray
     */
    public static JSONArray camel2LowerUnderScore(List list){
        return JSON.parseArray(JSON.toJSONString(list, (NameFilter) (object, name, value) -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)));
    }

}
