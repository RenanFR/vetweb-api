package com.vetweb.api.tests.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.vetweb.api.tests.kafka.KafkaTests.Porn;

public class KafkaTestsConsumer {
	
	@FunctionalInterface
	interface ConsumePornFunction<T> {
		
		void consume(ConsumerRecord<String, T> message);
		
	}	
	
	static class JerkingOffConsumer<T> {
		
		public void jerkOff(ConsumerRecord<String, T> record) {
			System.out.println(String.format("Watching %s video and masturbating", record.value()));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Done, time to cry");
		}
		
	}
	
	static class PornConsumerBuilder<T> {
		
		private final KafkaConsumer<String, T> consumer;
		private final ConsumePornFunction<T> pornFunction;
		
		public PornConsumerBuilder(String topic, String group, ConsumePornFunction<T> pornFunction, Class<T> type, Map<String, String> propertiesOverride) {
			this.pornFunction = pornFunction;
			this.consumer = new KafkaConsumer<>(propertiesConsumer(group, type, propertiesOverride));
			this.consumer.subscribe(List.of(topic));
		}
		
		public void startConsuming() {
				while(true) {
					ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
					if (!records.isEmpty()) {
						System.out.println(String.format("Found %d messages to process", records.count()));
						records.forEach(record -> {
							pornFunction.consume(record);
						});
					}
				}
		}

	}	
	
	private static Properties propertiesConsumer(String group, Class type, Map<String, String> propertiesOverride) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaTests.SERVER);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group);
		properties.setProperty(CustomDeserializer.TYPE_CONFIG, type.getName());
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, KafkaTests.class.getSimpleName() + "." + UUID.randomUUID().toString());
		properties.putAll(propertiesOverride);
		return properties;
	}	

	public static void main(String[] args) {
//		JerkingOffConsumer<String> jerkingOffConsumer = new JerkingOffConsumer<>();
		JerkingOffConsumer<Porn> jerkingOffConsumer = new JerkingOffConsumer<>();
//		new PornConsumerBuilder<String>(KafkaTests.TOPIC, jerkingOffConsumer.getClass().getName(), jerkingOffConsumer::jerkOff, String.class, Map.of()).startConsuming();
		new PornConsumerBuilder<Porn>(KafkaTests.TOPIC, jerkingOffConsumer.getClass().getName(), jerkingOffConsumer::jerkOff, Porn.class, Map.of()).startConsuming();
		
	}
	
}
