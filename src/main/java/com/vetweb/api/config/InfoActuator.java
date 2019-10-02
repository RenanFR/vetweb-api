package com.vetweb.api.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class InfoActuator implements InfoContributor {
	
	@Override
	public void contribute(Builder builder) {
		Map<String, Object> details = new HashMap<>();
		Properties properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("info.properties"));
			properties.entrySet().forEach((entry) -> {
				details.put(entry.getKey().toString(), entry.getValue());
			});
		} catch (IOException exception) {
		}
		builder.withDetails(details);
	}

}
