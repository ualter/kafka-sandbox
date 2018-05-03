package com.kafka.sandbox;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.kafka.sandbox.spring.Customer;

public class SimpleConsumerCustomer {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "local-test");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        KafkaConsumer<String, Customer> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("customerTopic"));
        
        try {
	        while (true) {
	        	System.out.println("AAA");
	            ConsumerRecords<String, Customer> records = consumer.poll(1000);
	            System.out.println("BBB");
	            for (ConsumerRecord<String, Customer> record : records) {
	                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
	                System.out.println("Customer:" + record.value());
	                System.out.println("");
	            }
	            
	        }
        } finally {
        	consumer.close();
        }
        
        
	}

}
