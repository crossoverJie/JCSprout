package com.crossoverjie.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/11/20 02:36
 * @since JDK 1.8
 */
public class KafkaTest {


    @Test
    public void consumer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<TopicPartition> tPartitions = new ArrayList<>();
        TopicPartition tPartition0 = new TopicPartition("data-push", 0);
        TopicPartition tPartition1 = new TopicPartition("data-push", 1);
        TopicPartition tPartition2 = new TopicPartition("data-push", 2);
        tPartitions.add(tPartition0);
        tPartitions.add(tPartition1);
        tPartitions.add(tPartition2);
        consumer.assign(tPartitions);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s,partition = %s value = %s%n", record.offset(), record.key(),record.partition(), record.value());

            }
        }
    }

    @Test
    public void threadConsumer() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        TopicPartition tPartition0 = new TopicPartition("data-push", 0);
        Consumer c1 = new Consumer(tPartition0,props,"c1");

        TopicPartition tPartition1 = new TopicPartition("data-push", 1);
        Consumer c2 = new Consumer(tPartition1,props,"c2");

        TopicPartition tPartition2 = new TopicPartition("data-push", 2);
        Consumer c3 = new Consumer(tPartition2,props,"c3");

        c1.start();
        c2.start();
        c3.start();

        c1.join();
        c2.join();
        c3.join();

    }

    private class Consumer extends Thread{
        private TopicPartition topicPartition ;
        private Properties props;

        private KafkaConsumer<String, String> consumer ;
        private List<TopicPartition> tPartitions = new ArrayList<>();

        public Consumer(TopicPartition topicPartition, Properties props,String name) {
            super(name);
            this.topicPartition = topicPartition;
            this.props = props;
            consumer = new KafkaConsumer<>(props) ;
            tPartitions.add(topicPartition) ;
        }

        @Override
        public void run() {
            consumer.assign(tPartitions);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records){
                    System.out.printf("thread= %s , offset = %d, key = %s,partition = %s value = %s%n",Thread.currentThread().getName(), record.offset(), record.key(),record.partition(), record.value());

                }
            }
        }
    }
}
