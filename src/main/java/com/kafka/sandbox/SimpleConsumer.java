package com.kafka.sandbox;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SimpleConsumer {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "local-test");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("streams-plaintext-input"));
        
        try {
	        while (true) {
	            ConsumerRecords<String, String> records = consumer.poll(100);
	            for (ConsumerRecord<String, String> record : records) {
	                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
	                System.out.println("");
	            }
	            
	        }
        } finally {
        	consumer.close();
        }
        
        
	}

}
