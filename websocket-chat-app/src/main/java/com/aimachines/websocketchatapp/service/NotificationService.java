package com.aimachines.websocketchatapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aimachines.websocketchatapp.model.ChatMessage;
import com.aimachines.websocketchatapp.model.SequenceDTO;
import com.aimachines.websocketchatapp.repository.ChatMessageRepository;
import com.aimachines.websocketchatapp.model.ChatMessage.MessageType;

@Service
public class NotificationService {
	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	public NotificationService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Autowired
	private Environment env;
	
	@Autowired 
	private ChatMessageRepository chatRepository;
	
	static String messagetemplate1="Hi ${message.username} Welcome to AI machines. I am your assistant today. How can I assist you?";
	static String messagetemplate2="Hi ${message.username} State your problem Sir, so I can assist you better.";
	static String messagetemplate3="Hi ${message.username} Please reply back or else this session will be over if you do not respond in next few minute";

	
	 // public NotificationService() { super(); this.messagingTemplate = null; }
	 
	public void sendPrivateNotification(final String username, SequenceDTO sequenceDto) {
		// OutputMessage message = new OutputMessage("Private Notification","","");
		final String time = new SimpleDateFormat("HH:mm").format(new Date());
		ChatMessage toCustomer = new ChatMessage();
		toCustomer.setSender(sequenceDto.getEmployeeName());
		toCustomer.setContent(fetchMessage(sequenceDto.getMessageTemplate(), username));
		toCustomer.setTime(time);
		toCustomer.setType(MessageType.CHAT);
		toCustomer.setReceiver(username);
		toCustomer.setUserName(username);
		//save messages to database
		
		chatRepository.save(toCustomer);
		//Sending messaget to the ui
		messagingTemplate.convertAndSend("/topic/messages/" + username, toCustomer);
	}

	private String fetchMessage(String message, String userName) {
		String msgFromProp =  "";//env.getProperty(message);
		if(message.contains("1")) {
			msgFromProp = messagetemplate1;
		}else if(message.contains("2")) {
			msgFromProp = messagetemplate2;
		}else {
			msgFromProp = messagetemplate3;
		}
		
		// Build map
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("message.username", userName);

		// Build StringSubstitutor
		StringSubstitutor sub = new StringSubstitutor(valuesMap);

		// Replace
		String finalFormattedMessage = sub.replace(msgFromProp);
		System.out.println(finalFormattedMessage);
		return finalFormattedMessage;

	}

	
	/*
	 * public static void main(String[] args) { NotificationService service = new
	 * NotificationService(); service.fetchMessage("message.template1","Richard, ");
	 * }
	 */
}