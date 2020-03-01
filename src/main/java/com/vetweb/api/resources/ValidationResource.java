package com.vetweb.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("validation")
public class ValidationResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("cep/{cep}")
	public ResponseEntity<Boolean> isValidCep(@PathVariable("cep") String cep) {
		boolean valid;
		try {
			String request = "http://viacep.com.br/ws/" + cep + "/json";
			String address = restTemplate.getForEntity(request, String.class).getBody();
			valid = address.contains("logradouro");
		} catch (Exception e) {
			valid = false;
			e.printStackTrace();
		}
		return ResponseEntity.ok(valid);
	}
	
}
