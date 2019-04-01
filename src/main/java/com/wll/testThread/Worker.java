package com.wll.testThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public abstract class Worker implements Runnable {
    private static final Logger _LOG = LoggerFactory.getLogger(Worker.class);


    /**
     * 信号量 主线程等待子线程完成任务
     */
    protected CountDownLatch signal;

    protected String parentThreadName;

    public String getParentThreadName() {
        return parentThreadName;
    }

    public void setParentThreadName(String parentThreadName) {
        this.parentThreadName = parentThreadName;
    }

    public CountDownLatch getSignal() {
        return signal;
    }

    public void setSignal(CountDownLatch signal) {
        this.signal = signal;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        doRun();
        _LOG.info("**** {} *** cost  ***  {}", who(), System.currentTimeMillis() - startTime);
    }

    protected abstract void doRun();

    public abstract String who();


    @Override
    public String toString() {
        return "Worker{" +
                "signal=" + signal +
                ", parentThreadName='" + parentThreadName + '\'' +
                '}';
    }
}
