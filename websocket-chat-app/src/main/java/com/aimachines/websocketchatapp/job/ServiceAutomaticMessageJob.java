package com.aimachines.websocketchatapp.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.aimachines.websocketchatapp.model.SequenceDTO;
import com.aimachines.websocketchatapp.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceAutomaticMessageJob  extends QuartzJobBean {

	@Autowired
	private NotificationService notificationService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Service Message Job Start................");
        String userName = (String) context.getJobDetail().getKey().getGroup();
        SequenceDTO sequenceDto = (SequenceDTO)context.getJobDetail().getJobDataMap().get("sequenceObject");     
        notificationService.sendPrivateNotification(userName,sequenceDto);
        log.info("Service Message Job End................");
    }
	
}
