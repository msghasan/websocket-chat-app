package com.aimachines.websocketchatapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aimachines.websocketchatapp.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByUserName(String sender);

	void deleteAllByUserName(String userName);

	

}
