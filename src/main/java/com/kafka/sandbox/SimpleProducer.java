package com.kafka.sandbox;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<>(props);
        
        producer.send(new ProducerRecord<String, String>("streams-plaintext-input", "ualter"));
        producer.send(new ProducerRecord<String, String>("streams-plaintext-input", "ualter"));

        producer.close();
        
        System.out.println("End producer...");
        
	}

}
