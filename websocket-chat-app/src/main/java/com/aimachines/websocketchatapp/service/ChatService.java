package com.aimachines.websocketchatapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aimachines.websocketchatapp.model.ChatMessage;
import com.aimachines.websocketchatapp.repository.ChatMessageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ChatService {

	@Autowired
	private SchedulerJobService jobService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired 
	private ChatMessageRepository chatRepository;
	
	public void sendMessageToCustomer(ChatMessage chatMessagePojo, SimpMessageHeaderAccessor headerAccessor) {
		final String time = new SimpleDateFormat("HH:mm").format(new Date());
		String userName = chatMessagePojo.getSender();
		chatMessagePojo.setUserName(userName);
		chatMessagePojo.setTime(time);
		jobService.deleteJobByGroupName(userName);
		 chatRepository.deleteAllByUserName(userName);
			log.info("Deleted all chat history for user " + userName);
		

		log.info("Response After ");
		messagingTemplate.convertAndSend("/topic/messages/" + chatMessagePojo.getSender(), chatMessagePojo);

	}
	
	
	public void addUserAndStartAutomatedMessage(ChatMessage chatMessagePojo, 
			SimpMessageHeaderAccessor headerAccessor) throws Exception {
		// TODO Auto-generated method stub
		log.info("Adding user with username :" + chatMessagePojo.getSender());

		List<ChatMessage> chatmessages = checkIfPreviousMessageAvailable(chatMessagePojo);
		if (null != chatmessages && chatmessages.size() > 0) {
			log.info("Restoring previous Messages for User");
			for (ChatMessage chatMessage : chatmessages) {
				messagingTemplate.convertAndSend("/topic/messages/" + chatMessagePojo.getSender(), chatMessage);
			}

		} else {
			// Add username in web socket session
			sendMessageWithScheduledJobs(chatMessagePojo, headerAccessor);
		}
	}


	private void sendMessageWithScheduledJobs(ChatMessage chatMessagePojo, SimpMessageHeaderAccessor headerAccessor)
			throws Exception {
		String userName = chatMessagePojo.getSender();
		headerAccessor.getSessionAttributes().put("username", userName);
		jobService.scheduleAllJobs(userName);
		messagingTemplate.convertAndSend("/topic/messages/" + chatMessagePojo.getSender(), chatMessagePojo);
	}
	
	
	private List<ChatMessage> checkIfPreviousMessageAvailable(ChatMessage chatMessagePojo) {
		return chatRepository.findByUserName(chatMessagePojo.getSender());
	}


	public void joinUserToCommunication(String to, ChatMessage message) {
		log.info("Send Message to User "+to);
    	messagingTemplate.convertAndSend("/topic/messages/"+to,message);
		
	}

	public void deleteAllChatOfAllUsers() {
		 chatRepository.deleteAll();
	}


	public void deleteAllChatForUser(String userName) {
		 chatRepository.deleteAllByUserName(userName);
		
	}
	
}
