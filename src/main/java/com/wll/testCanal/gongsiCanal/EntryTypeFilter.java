package com.wll.testCanal.gongsiCanal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wll.testCanal.gongsiCanal.service.MessageFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

@Component
public class EntryTypeFilter implements MessageFilter {

    @Value("#{${aggregation.source.tables}}")
    private List<String> sourceTables;

    @Override
    public void filter(List<CanalEntry.Entry> entries) {
        if (CollectionUtils.isEmpty(entries)) {
            return;
        }
        Iterator<CanalEntry.Entry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            CanalEntry.Entry next = iterator.next();
            if (!filter(next)) {
                iterator.remove();
            }
        }

    }

    /**
     * 判定Entry是否需要处理
     */
    @Override
    public boolean filter(CanalEntry.Entry entry) {
        if (entry != null && entry.getEntryType() == CanalEntry.EntryType.ROWDATA
                && entry.getHeader().getEventType() != CanalEntry.EventType.QUERY
                && entry.getHeader().getEventType() != CanalEntry.EventType.DELETE
                // && entry.getHeader().getEventType() != CanalEntry.EventType.UPDATE
                && sourceTables.contains(entry.getHeader().getTableName())) {//select 也要过滤掉
            return true;
        }
        return false;
    }
}