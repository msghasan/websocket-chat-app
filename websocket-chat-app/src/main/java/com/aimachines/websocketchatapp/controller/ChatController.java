package com.aimachines.websocketchatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.aimachines.websocketchatapp.model.ChatMessage;
import com.aimachines.websocketchatapp.service.ChatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatController {


	
	@Autowired 
	private ChatService chatService;
	
	
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessagePojo, SimpMessageHeaderAccessor headerAccessor) {
    	chatService.sendMessageToCustomer(chatMessagePojo,headerAccessor);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessagePojo, SimpMessageHeaderAccessor headerAccessor) throws Exception {
       chatService.addUserAndStartAutomatedMessage(chatMessagePojo,headerAccessor);
    }
    
    @MessageMapping("/chatEndPoint/{to}")
  //  @SendToUser("/queue/private-messages/")
    public void sendMessage(@DestinationVariable String to , ChatMessage message) throws Exception {
    	chatService.joinUserToCommunication(to,message);
        
    }
}