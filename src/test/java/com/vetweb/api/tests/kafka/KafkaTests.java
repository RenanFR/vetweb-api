package com.vetweb.api.tests.kafka;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaTests {
	
	public static final String SERVER = "0.0.0.0:9092";
	public static final String TOPIC = "PORN.UPLOAD";
	
//	static class PornVideoUploadNotification implements AutoCloseable {
	static class PornVideoUploadNotification {
		
		private final KafkaProducer<String, String> producer = new KafkaProducer<>(properties());
		
		
		public void uploadNewPorn() throws InterruptedException, ExecutionException {
//			try (producer) {
				for (int i = 0; i <= 3; i++) {
					var key = "BLOWJOB " + UUID.randomUUID().toString();
					var value = "Amazing blonde making blow job";
					ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);
					Callback callback = (recordMetadata, exception) -> {
						if (exception != null) {
							exception.printStackTrace();
							return;
						}
						System.out.println(String.format("Sending message to topic %s, partition %s, message counter at %s, sent at %s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp())); 
					};
					producer.send(record, callback).get();
				}
//			}
		}
		
//		@Override
//		public void close() {
//			producer.close();
//		}
		
	}
	
	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new PornVideoUploadNotification().uploadNewPorn();
	}
	
}
