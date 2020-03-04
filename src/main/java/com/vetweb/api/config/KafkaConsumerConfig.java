package com.vetweb.api.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.vetweb.api.model.mongo.Message;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	
	@Value(value = "${kafka.address}")
	private String kafkaAddress;
	
	@Bean
	public ConsumerFactory<String, Message> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress, 
				ConsumerConfig.GROUP_ID_CONFIG, KafkaMessageListener.class.getName(), 
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class));
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
	
}
