package com.wll.testCanal.gongsiCanal.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class SimpleMailSenderUtil {

    private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final SimpleMailSender SIMPLE_MAIL_SENDER = new SimpleMailSender();

    @Value("${mail_subject}")
    private String env;
    @Value("${mail_host}")
    private String mailHost;
    @Value("${mail_user}")
    private String fromAddress;
    @Value("${mail_user}")
    private String mailUserName;
    @Value("${mail_pass}")
    private String mailPassword;
    @Value("${to_address}")
    private String[] toAddresses;

    public void sendMail(String subject, String context){
        //支持多人
        if(toAddresses != null){
            for(String toAddress : toAddresses){
                MailSenderInfo mailSenderInfo = new MailSenderInfo();
                mailSenderInfo.setMailServerHost(mailHost);
                mailSenderInfo.setFromAddress(fromAddress);
                mailSenderInfo.setValidate(true);
                mailSenderInfo.setUserName(mailUserName);
                mailSenderInfo.setPassword(mailPassword);
                mailSenderInfo.setSubject(env + " " + subject);
                mailSenderInfo.setContent(context);
                mailSenderInfo.setToAddress(toAddress);
                SendTextMailHandler sendTextMailHandler = new SendTextMailHandler(mailSenderInfo);
                EXECUTOR.execute(sendTextMailHandler);
            }
        }
    }

    private class SendTextMailHandler implements Runnable{

        private MailSenderInfo mailSenderInfo;

        public SendTextMailHandler(MailSenderInfo mailSenderInfo) {
            this.mailSenderInfo = mailSenderInfo;
        }

        @Override
        public void run() {
            SIMPLE_MAIL_SENDER.sendTextMail(mailSenderInfo);
        }
    }

}
