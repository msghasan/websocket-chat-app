package com.aimachines.websocketchatapp.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aimachines.websocketchatapp.model.SchedulerJobInfo;
import com.aimachines.websocketchatapp.service.ChatService;
import com.aimachines.websocketchatapp.service.SchedulerJobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class IndexController {


	/*
	 * @GetMapping("/index") public String index(Model model){
	 * 
	 * return "index"; }
	 */
	@Autowired
	private SchedulerJobService scheduleJobService;
	
	@Autowired
	private ChatService chatService;
	
	
	@RequestMapping(value = "/deleteJobs", method = { RequestMethod.GET, RequestMethod.POST })
	public Object deleteJob(String jobName) {
		
		try {
			scheduleJobService.deleteJobByGroupName(jobName);
			scheduleJobService.deleteJob(jobName);
		} catch (Exception e) {
			log.error("deleteJob ex:", e);
		}
		return "Deleted";
	}
	
	@RequestMapping("/getAllJobs")
	public Object getAllJobs() throws SchedulerException {
		List<SchedulerJobInfo> jobList = scheduleJobService.getAllJobList();
		return jobList;
	}
	
	@RequestMapping("/deleteAllChats/{userName}")
	public Object deleteAllChatOfAllUsers(@DestinationVariable String userName) throws SchedulerException {
		try {
		if(null == userName || userName =="")
		chatService.deleteAllChatOfAllUsers();
		
		else
			chatService.deleteAllChatForUser(userName);
		}catch(Exception e) {
			return "Not Deleted";
		}
		return "Deleted";
	}
	
}
