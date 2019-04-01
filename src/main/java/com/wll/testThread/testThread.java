package com.wll.testThread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class testThread {
    public static void  main(String[] ags){
        CountDownLatch signal = SignalUtils.getSignal(1);
        QualityWorker qualityWorker = QualityWorker.QualityWorkerBuilder.newBuilder()
                .buildSignal(signal).build();
        Executor.addWorker(qualityWorker);
        try {
            signal.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
