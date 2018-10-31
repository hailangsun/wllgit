package com.wll.testCanal.gongsiCanal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolUtil {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void executeTask(Runnable task){
        threadPoolTaskExecutor.execute(task);
    }

}
