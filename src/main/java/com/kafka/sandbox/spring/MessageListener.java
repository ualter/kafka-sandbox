package com.kafka.sandbox.spring;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class MessageListener {

	public CountDownLatch messageLatch  = new CountDownLatch(1);
	public CountDownLatch customerLatch = new CountDownLatch(1);

	
	@KafkaListener(topics = "${message.topic.name}",group = "foo",containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(String message) {
        System.out.println("Received Messasge in group 'foo': " + message);
        this.messageLatch.countDown();
    }
	
	@KafkaListener(topics = "${customer.topic.name}",containerFactory = "costumerKafkaListenerContainerFactory")
	public void customerListener(Customer customer) {
		System.out.println("Recieved greeting message: " + customer);
		this.customerLatch.countDown();
	}

}
