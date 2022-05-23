package com.aimachines.websocketchatapp.listeners;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimachines.websocketchatapp.model.SchedulerJobInfo;
import com.aimachines.websocketchatapp.repository.SchedulerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobEventListener implements JobListener {

	public static final String LISTENER_NAME = "ResponseListener";



	@Autowired
	private SchedulerRepository schedulerRepository;
	
	@Override
	public String getName() {
		return LISTENER_NAME; //must return a name
	}

	// Run this if job is about to be executed.
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {

		String jobName = context.getJobDetail().getKey().toString();
		log.info("jobToBeExecuted");
		log.info("Job : " + jobName + " is going to start...");

	}

	// No idea when will run this?
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info("jobExecutionVetoed");
	}

	//Run this after job has been executed
	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		log.info("jobWasExecuted");
		String jobName = context.getJobDetail().getKey().toString();
		SchedulerJobInfo jobInfo = schedulerRepository.findByJobName(jobName);
		jobInfo.setExecutedTime(System.currentTimeMillis());
		schedulerRepository.save(jobInfo);
		
		log.info("Job : " + jobName + " is finished...");

	}

}