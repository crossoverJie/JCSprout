package com.crossoverjie.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/8/29 01:42
 * @since JDK 1.8
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}