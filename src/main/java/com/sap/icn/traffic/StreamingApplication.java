package com.sap.icn.traffic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by I321761 on 2017/7/21.
 */
public class StreamingApplication {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf()
                .setMaster("local[2]") // local mode with 2 threads
                .setAppName("RealtimeSpeedCalculator");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sc, new Duration(60 * 1000L));

        // Kafka configuration
        Map<String, Object> kafkaParams = new HashMap();
        kafkaParams.put("bootstrap.servers", "10.128.184.199:9121");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", 0);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("topic-taxi");
        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );

        stream.map(record -> {
            System.out.println("#############");
            return record.value();
        }).count();

//        streamingContext.start();
    }
}
