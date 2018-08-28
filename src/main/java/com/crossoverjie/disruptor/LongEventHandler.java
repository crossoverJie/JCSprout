package com.crossoverjie.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/8/29 01:43
 * @since JDK 1.8
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event.getValue());
    }
}
