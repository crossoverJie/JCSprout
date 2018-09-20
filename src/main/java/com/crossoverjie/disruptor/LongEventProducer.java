package com.crossoverjie.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/8/29 01:43
 * @since JDK 1.8
 */
public class LongEventProducer {
    private final static Logger LOGGER = LoggerFactory.getLogger(LongEventProducer.class);
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            long aLong = bb.getLong(0);
            LOGGER.info("product=[{}]",aLong);
            event.set(aLong);  // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}