package com.kafka.sandbox;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;


public class SimpleConsumerTopicA {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "local-test");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        
        String topicName = "topicA";
        consumer.subscribe(Arrays.asList(topicName));
        
		TopicPartition tp0 = new TopicPartition(topicName, 0);
        TopicPartition tp1 = new TopicPartition(topicName, 1);
		consumer.poll(0);
        //consumer.seekToBeginning(Arrays.asList(tp0,tp1));
        
		System.out.println("consumer.assignment()............: " + consumer.assignment());
		System.out.println("consumer.position(tp0)...........: " + consumer.position(tp0));
		System.out.println("consumer.position(tp1)...........: " + consumer.position(tp1));
		System.out.println("consumer.endOffsets(tp0).........: " + consumer.endOffsets(Arrays.asList(tp0,tp1)));
		System.out.println("consumer.endOffsets(tp1).........: " + consumer.endOffsets(Arrays.asList(tp0,tp1)));
		
		
        try {
	        while (true) {
	            ConsumerRecords<String, String> records = consumer.poll(100);
	            for (ConsumerRecord<String, String> record : records) {
	                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
	                System.out.println("\n*************************************************************************");
	            }
	        }
        } finally {
        	consumer.close();
        }
        
        
	}

}
