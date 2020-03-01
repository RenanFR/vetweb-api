//package com.vetweb.api.resources;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.vetweb.api.model.Message;
//
//@Controller
//@RequestMapping("messages")
//public class MessageController {
//	
//	@MessageMapping("message.add")
//	@SendTo("/topic/test")
//	public Message register(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
//		headerAccessor.getSessionAttributes().put("user", message.getSender());
//		return message;
//	}
//	
//	@MessageMapping("message.send")
//	@SendTo("/topic/test")
//	public Message send(@Payload Message message) {
//		return message;
//	}
//
//}
