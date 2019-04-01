package com.wll.testThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class QualityWorker extends Worker {
    private static final Logger _LOG = LoggerFactory.getLogger(QualityWorker.class);

    private CountDownLatch pointWaitSignal;

    /**
     * 信号量 主线程等待子线程完成任务
     */
    protected CountDownLatch signal;

    @Override
    public void doRun() {
        try {

            System.out.println("线程执行："+Thread.currentThread().getName());

        } catch (Exception e) {
            _LOG.error("error", e);
        } finally {
            pointWaitSignal.countDown();
            signal.countDown();
            _LOG.info("qualityWorker:{}",this.toString());
        }


    }

    @Override
    public String who() {
        return "QualityWorker";
    }


    public CountDownLatch getPointWaitSignal() {
        return pointWaitSignal;
    }

    public void setPointWaitSignal(CountDownLatch pointWaitSignal) {
        this.pointWaitSignal = pointWaitSignal;
    }


    public static final class QualityWorkerBuilder {
        protected CountDownLatch signal;
        protected CountDownLatch pointWaitSignal;
        private List<String> taskIds;

        private QualityWorkerBuilder() {
        }

        public static QualityWorkerBuilder newBuilder() {
            return new QualityWorkerBuilder();
        }


        public QualityWorkerBuilder buildSignal(CountDownLatch signal) {
            this.signal = signal;
            return this;
        }


        public QualityWorkerBuilder buildTaskIds(List<String> taskIds) {
            this.taskIds = taskIds;
            return this;
        }

        public QualityWorkerBuilder buildPointWaitSignal(CountDownLatch pointWaitSignal) {
            this.pointWaitSignal = pointWaitSignal;
            return this;
        }


        public QualityWorker build() {
            QualityWorker qualityWorker = new QualityWorker();
            qualityWorker.setSignal(signal);
            qualityWorker.setPointWaitSignal(pointWaitSignal);
            qualityWorker.setParentThreadName(Thread.currentThread().getName());
            return qualityWorker;
        }
    }
}
