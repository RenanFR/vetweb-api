package com.vetweb.api.tests.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaTestsConsumer {
	
	@FunctionalInterface
	interface ConsumePornFunction {
		
		void consume(ConsumerRecord<String, String> message);
		
	}	
	
	static class JerkingOffConsumer {
		
		public void jerkOff(ConsumerRecord<String, String> record) {
			System.out.println(String.format("Watching %s video and masturbating", record.value()));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Done, time to cry");
		}
		
	}
	
//	static class PornConsumerBuilder implements AutoCloseable {
	static class PornConsumerBuilder {
		
		private final KafkaConsumer<String, String> consumer;
		private final ConsumePornFunction pornFunction;
		
		public PornConsumerBuilder(String topic, String group, ConsumePornFunction pornFunction) {
			this.pornFunction = pornFunction;
			this.consumer = new KafkaConsumer<>(propertiesConsumer(group));
			this.consumer.subscribe(List.of(topic));
		}
		
		public void startConsuming() {
//			try (consumer) {
				while(true) {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
					if (!records.isEmpty()) {
						System.out.println(String.format("Found %d messages to process", records.count()));
						records.forEach(record -> {
							pornFunction.consume(record);
						});
					}
				}
//			}
		}

//		@Override
//		public void close() throws Exception {
//			this.consumer.close();
//		}
		
	}	
	
	private static Properties propertiesConsumer(String group) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaTests.SERVER);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, KafkaTests.class.getSimpleName() + "." + UUID.randomUUID().toString());
		return properties;
	}	

	public static void main(String[] args) {
		JerkingOffConsumer jerkingOffConsumer = new JerkingOffConsumer();
		new PornConsumerBuilder(KafkaTests.TOPIC, jerkingOffConsumer.getClass().getName(), jerkingOffConsumer::jerkOff).startConsuming();
	}
	
}
