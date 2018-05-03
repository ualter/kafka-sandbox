package com.kafka.sandbox.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
	
	 @Autowired
     private KafkaTemplate<String, String> kafkaTemplate;
	 
	 @Autowired
     private KafkaTemplate<String, Customer> customerKafkaTemplate;
	 
     @Value(value = "${message.topic.name}")
     private String messageTopicName;
     
     @Value(value = "${customer.topic.name}")
     private String customerTopicName;
	 
     
     public void sendMessage(String message) {
         kafkaTemplate.send(messageTopicName, message);
     }
     
     
//     public void sendMessageToPartion(String message, int partition) {
//         kafkaTemplate.send(partionedTopicName, partition, message);
//     }
//
//     public void sendMessageToFiltered(String message) {
//         kafkaTemplate.send(filteredTopicName, message);
//     }
	 
     public void sendCustomer(Customer customer) {
    	 customerKafkaTemplate.send(customerTopicName, customer);
     }

}
