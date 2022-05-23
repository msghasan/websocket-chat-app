package com.aimachines.websocketchatapp.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.aimachines.websocketchatapp.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class SampleCronJob extends QuartzJobBean {
	@Autowired
	private NotificationService notificationService;
	

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("SampleCronJob Start................");
        String userName = (String) context.getJobDetail().getKey().getName();
    //    notificationService.sendPrivateNotification(userName);
        log.info("SampleCronJob End................");
    }
}
