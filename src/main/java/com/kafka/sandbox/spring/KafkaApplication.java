package com.kafka.sandbox.spring;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KafkaApplication {
	
	
	public static void main(String[] args) throws Exception {
		
		ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);
		
		MessageProducer producer = context.getBean(MessageProducer.class);
//        MessageListener listener = context.getBean(MessageListener.class);
		
		
        producer.sendMessage("Hello To Topic AAAAAAAAAAAAAA!");
        producer.sendCustomer(new Customer(1,"Joe",Date.from(Instant.now())));
        
        
//        listener.customerLatch.await(10, TimeUnit.SECONDS);
        context.close();
	}

}
