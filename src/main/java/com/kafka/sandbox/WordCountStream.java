package com.kafka.sandbox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

public class WordCountStream {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, "localhost:7070");
        
        final StreamsBuilder builder = new StreamsBuilder();
        
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
	        .groupBy((key, value) -> value)
	        .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
	        .toStream()
	        .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
        
        
        final Topology topology = builder.build();
        System.out.println(topology.describe());
        
        final KafkaStreams   streams       = new KafkaStreams(topology, props);
        final CountDownLatch latch         = new CountDownLatch(1);
        
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        
        try {
            streams.start();
            interactiveQuerySearchWords(streams, latch);
            latch.await();
        } catch (Throwable e) {
        	e.printStackTrace();
            System.exit(1);
        }
		
	}

	private static void interactiveQuerySearchWords(final KafkaStreams streams, final CountDownLatch latch) throws InterruptedException {
		Thread.sleep(5000);
		Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				t.setName("SearchWord-Kafka");
				return t;
			}
		}).execute(() -> {
			Map<String,Long> searchWord = new HashMap<String,Long>();
			searchWord.put("ualter", 0L);
			searchWord.put("kafka", 0L);
			searchWord.put("hello", 0L);
			searchWord.put("world", 0L);
			searchWord.put("all", 0L);
			
			while (latch.getCount() == 1) {
		    	ReadOnlyKeyValueStore<String, Long> keyValueStore = streams.store("counts-store", QueryableStoreTypes.keyValueStore());
		    	
		    	searchWord.forEach((k,v) -> {
		    		Long timeWord = keyValueStore.get(k);
		    		if (timeWord > 0 && timeWord != v) {
		    			System.out.println(String.format("[%s] %s = %d",Thread.currentThread().getName(),k,timeWord));
		    			searchWord.put(k, timeWord);
		    		}
		    	});
		    	
		        try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
