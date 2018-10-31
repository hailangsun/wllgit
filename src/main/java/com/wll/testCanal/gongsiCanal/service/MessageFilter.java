package com.wll.testCanal.gongsiCanal.service;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

public interface MessageFilter {

    boolean filter(CanalEntry.Entry entry);

    void filter(List<CanalEntry.Entry> entries);

}
