
package com.wll.demo.Excel4j.converter;

/**
 * 写入excel内容转换器
 */
public interface ReadConvertible {

    /**
     * 读取Excel列内容转换
     *
     * @param object 待转换数据
     * @return 转换完成的结果
     */
    Object execRead(String object);
}
