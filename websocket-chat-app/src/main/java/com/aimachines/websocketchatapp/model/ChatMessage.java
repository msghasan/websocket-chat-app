package com.aimachines.websocketchatapp.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name="chatMessage")
@ToString
@EqualsAndHashCode
public class ChatMessage {
		@Id
		@GeneratedValue
		private Long id;
		@Enumerated(EnumType.STRING)
	    private MessageType type;
	    private String content;
	    private String sender;
	    private String time;
	    private String receiver;
	    private String userName;
	    private String isActive;
	    
	    
	    public enum MessageType {
	        CHAT,
	        JOIN,
	        LEAVE
	    }
	}