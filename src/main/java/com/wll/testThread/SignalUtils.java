package com.wll.testThread;

import java.util.concurrent.CountDownLatch;

public class SignalUtils {


    public static CountDownLatch getSignal(int size) {
        return new CountDownLatch(size);
    }
}