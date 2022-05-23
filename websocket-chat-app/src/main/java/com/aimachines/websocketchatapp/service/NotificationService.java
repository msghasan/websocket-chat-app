package com.aimachines.websocketchatapp.service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aimachines.websocketchatapp.constants.ConstantMessages;
import com.aimachines.websocketchatapp.model.ChatMessage;
import com.aimachines.websocketchatapp.model.ChatMessage.MessageType;
import com.aimachines.websocketchatapp.model.SequenceDTO;
import com.aimachines.websocketchatapp.repository.ChatMessageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
	private final SimpMessagingTemplate messagingTemplate;
	
	static Map <String, String > stringMap = new HashMap<>();
	static  {
		
		/*
		 * stringMap.put("default1", ConstantMessages.default1);
		 * stringMap.put("default2", ConstantMessages.default2);
		 * stringMap.put("default3", ConstantMessages.default3);
		 * 
		 * stringMap.put("sales1", ConstantMessages.sales1); stringMap.put("sales2",
		 * ConstantMessages.sales2); stringMap.put("sales3", ConstantMessages.sales3);
		 * 
		 * stringMap.put("service1", ConstantMessages.service1);
		 * stringMap.put("service2", ConstantMessages.service2);
		 * stringMap.put("service3", ConstantMessages.service3);
		 */
			Field[] fields = ConstantMessages.class.getFields();
			ConstantMessages message = new ConstantMessages();
			 for(Field field:fields) {
				 try {
					 
					stringMap.put(field.getName(),field.get(message).toString());
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			
	}

	@Autowired
	public NotificationService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	
	@Autowired 
	private ChatMessageRepository chatRepository;
	
	
	
	// public NotificationService() { super(); this.messagingTemplate = null; }
	 
	public void sendPrivateNotification(final String username, SequenceDTO sequenceDto) {
		// OutputMessage message = new OutputMessage("Private Notification","","");
		final String time = new SimpleDateFormat("HH:mm").format(new Date());
		ChatMessage toCustomer = new ChatMessage();
		toCustomer.setSender(sequenceDto.getEmployeeName());
		toCustomer.setContent(fetchMessage(sequenceDto, username));
		toCustomer.setTime(time);
		toCustomer.setType(MessageType.CHAT);
		toCustomer.setReceiver(username);
		toCustomer.setUserName(username);
		//save messages to database
		
		chatRepository.save(toCustomer);
		//Sending messaget to the ui
		messagingTemplate.convertAndSend("/topic/messages/" + username, toCustomer);
	}

	private String fetchMessage(SequenceDTO seqDto, String userName) {
		String msgFromProp =  "";//env.getProperty(message);
		msgFromProp = getTypeOfTemplate(seqDto);
		
		// Build map
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("message.username", userName);

		// Build StringSubstitutor
		StringSubstitutor sub = new StringSubstitutor(valuesMap);

		// Replace
		String finalFormattedMessage = sub.replace(msgFromProp);
		log.info("Final formatted message for customer : "+finalFormattedMessage);
		return finalFormattedMessage;

	}

	private String getTypeOfTemplate(SequenceDTO seqDto) {
		
			return stringMap.get(seqDto.getMessageTemplate());
	}

	
	
	/*
	 * public static void main(String[] args) { for(Map.Entry<String, String> set :
	 * stringMap.entrySet()) {
	 * System.out.println(set.getKey()+" : "+set.getValue()); }
	 * 
	 * 
	 * }
	 */
	 
}