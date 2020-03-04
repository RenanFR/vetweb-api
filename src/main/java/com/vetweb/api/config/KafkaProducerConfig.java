package com.vetweb.api.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.vetweb.api.model.mongo.Message;

@Configuration
public class KafkaProducerConfig {
	
	@Value(value = "${kafka.address}")
	private String kafkaAddress;
	
	@Bean
	public ProducerFactory<String, Message> producerFactory() {
		Map<String, Object> configProps = Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);
		return new DefaultKafkaProducerFactory< >(configProps);
	}
	
	@Bean
	public KafkaTemplate<String, Message> kafkaTemplate() {
		return new KafkaTemplate<String, Message>(producerFactory());
	}

}
