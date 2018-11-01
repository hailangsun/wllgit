package com.wll.testCanal.gongsiCanal;

import com.wll.testCanal.gongsiCanal.mail.SimpleMailSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InitAfterSpring {
    private static final Logger logger = LoggerFactory.getLogger(InitAfterSpring.class);

    @Resource(name = "dataRecv")
    private AbstractDataRecv dataRecv;

    @Autowired
    private SimpleMailSenderUtil simpleMailSenderUtil;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Executors.newSingleThreadExecutor(new ThreadFactory() {
            private AtomicInteger atomicInteger = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "dataRecv-polestar-channel" + atomicInteger.get());
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("****canal connect 线程出现问题 ******",e);
                        simpleMailSenderUtil.sendMail("dataRecv-polestar-channel-uncaughtException", e + "");
                    }
                });
                return thread;
            }
        }).execute(new Runnable() {
            @Override
            public void run() {
                dataRecv.start();
            }
        });
    }
}
