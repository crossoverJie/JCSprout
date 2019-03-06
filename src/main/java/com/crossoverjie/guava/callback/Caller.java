package com.crossoverjie.guava.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/7/15 13:52
 * @since JDK 1.8
 */
public class Caller {

    private final static Logger LOGGER = LoggerFactory.getLogger(Caller.class);

    private CallBackListener callBackListener ;

    private Notifier notifier ;

    private String question ;

    /**
     * 使用
     */
    public void call(){

        LOGGER.info("开始提问");

        //新建线程，达到异步效果
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    notifier.execute(Caller.this,question);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        LOGGER.info("提问完毕，我去干其他事了");
    }


    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }


    public CallBackListener getCallBackListener() {
        return callBackListener;
    }

    public void setCallBackListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Caller{" +
                "callBackListener=" + callBackListener +
                ", notifier=" + notifier +
                ", question='" + question + '\'' +
                '}';
    }
}
