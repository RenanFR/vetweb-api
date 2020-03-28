package com.vetweb.api.resources;

import static com.vetweb.api.pojo.DTOConverter.userToDataTransferObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.mongo.AutomaticResponse;
import com.vetweb.api.model.mongo.Availability;
import com.vetweb.api.model.mongo.ChatConfiguration;
import com.vetweb.api.model.mongo.Message;
import com.vetweb.api.model.mongo.WeekDay;
import com.vetweb.api.persist.mongo.ChatConfigurationRepository;
import com.vetweb.api.service.MessageService;
import com.vetweb.api.service.auth.UserService;

@RestController
@RequestMapping("chat")
public class ChatResource {
	
	@Autowired
	private MessageService service;
	
	@Autowired
	private ChatConfigurationRepository configurationRepository;
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<Message> send(@RequestBody Message msg) {
		Message sent = service.send(msg);
		return ResponseEntity.ok(sent);
	}
	
	@GetMapping("{user}")
	public ResponseEntity<List<Message>> messagesFromUser(@PathVariable("user") String user) {
		List<Message> messages = service.messagesFromUser(user);
		return ResponseEntity.ok(messages);
	}
	
	@GetMapping("search")
	public ResponseEntity<List<Message>> searchMessages(@RequestParam("searchTerm") String searchTerm) {
		List<Message> searchResult = this.service.findMessagesBySearchTerm(searchTerm);
		return ResponseEntity.ok(searchResult);
	}
	
	@PostMapping("config")
	public ResponseEntity<Boolean> saveConfig(@RequestBody ChatConfiguration configuration) {
		configurationRepository.save(configuration);
		return ResponseEntity.accepted().body(true);
	}
	
	@GetMapping("config/{user}")
	public ResponseEntity<ChatConfiguration> getConfig(@PathVariable("user") String user) {
		ChatConfiguration chatConfiguration = configurationRepository.findByUserUserEmail(user);
		if (chatConfiguration == null) {
			chatConfiguration = configurationRepository.save(ChatConfiguration
					.builder()
					.user(userToDataTransferObject(userService.findByEmail(user)))
					.changedAt(LocalDateTime.now())
					.automaticResponse(AutomaticResponse
							.builder()
							.enabled(false)
							.text("")
							.availability(Availability
									.builder()
									.enabled(false)
									.text("Estou fora do horário de atendimento")
									.sunday(defaultWeekDay("Domingo"))
									.monday(defaultWeekDay("Segunda"))
									.tuesday(defaultWeekDay("Terça"))
									.wednesday(defaultWeekDay("Quarta"))
									.thursday(defaultWeekDay("Quinta"))
									.friday(defaultWeekDay("Sexta"))
									.saturday(defaultWeekDay("Sábado"))
									.build())
							.build())
					.build());
		}
		return ResponseEntity.ok(chatConfiguration);
	}
	
	private WeekDay defaultWeekDay(String name) {
		return WeekDay.builder().name(name).enabled(false).startTime(LocalTime.of(0, 0)).endTime(LocalTime.of(23, 59)).build();
	}
	
}
