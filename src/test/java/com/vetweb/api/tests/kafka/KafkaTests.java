package com.vetweb.api.tests.kafka;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class KafkaTests {
	
	public static final String SERVER = "0.0.0.0:9092";
	public static final String TOPIC = "PORN.UPLOAD";
	
	@NoArgsConstructor
	@Getter
	@Setter
	static class Porn {
		
		private String title;
		private String content;
		private Long secondsDuration;
		
		public Porn(String title, String content, Long secondsDuration) {
			this.title = title;
			this.content = content;
			this.secondsDuration = secondsDuration;
		}
		
		
	}
	
	static class PornVideoUploadNotification<T> {
		
		private final KafkaProducer<String, T> producer = new KafkaProducer<>(properties());
		
		T value;
		
		public PornVideoUploadNotification(T value) {
			this.value = value;
		}
		
		public PornVideoUploadNotification() {
		}
		
		
		public void uploadNewPorn() throws InterruptedException, ExecutionException {
				for (int i = 0; i <= 3; i++) {
					var key = "BLOWJOB " + UUID.randomUUID().toString();
					ProducerRecord<String, T> record = new ProducerRecord<>(TOPIC, key, value);
					Callback callback = (recordMetadata, exception) -> {
						if (exception != null) {
							exception.printStackTrace();
							return;
						}
						System.out.println(String.format("Sending message to topic %s, partition %s, message counter at %s, sent at %s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp())); 
					};
					producer.send(record, callback).get();
				}
		}
		
	}
	
	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
		return properties;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		new PornVideoUploadNotification<String>("Amazing blonde making blow job").uploadNewPorn();
		new PornVideoUploadNotification<Porn>(new Porn("Amazing blonde making blow job", "Sex content", 60L)).uploadNewPorn();
	}
	
}
