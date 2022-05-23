package com.aimachines.websocketchatapp.listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.aimachines.websocketchatapp.model.ChatMessage;
import com.aimachines.websocketchatapp.service.SchedulerJobService;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
	private SchedulerJobService jobService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessagePojo = new ChatMessage();
            chatMessagePojo.setType(ChatMessage.MessageType.LEAVE);
            chatMessagePojo.setSender(username);
            
            jobService.deleteJobByGroupName(username);
            messagingTemplate.convertAndSend("/topic/public", chatMessagePojo);
        }
    }
    
    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
    	GenericMessage message = (GenericMessage) event.getMessage();
    //    String sender = message.getSender();
        logger.info("Response from Customer "+message);

        
    }
}