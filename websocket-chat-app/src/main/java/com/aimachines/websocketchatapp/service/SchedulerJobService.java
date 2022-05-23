package com.aimachines.websocketchatapp.service;

import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aimachines.websocketchatapp.listeners.JobEventListener;
import com.aimachines.websocketchatapp.model.SchedulerJobInfo;
import com.aimachines.websocketchatapp.repository.SchedulerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SchedulerJobService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private SchedulerRepository schedulerRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private JobScheduleCreator scheduleCreator;

	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}

	public List<SchedulerJobInfo> getAllJobList() {
		return schedulerRepository.findAll();
	}

	public void deleteJob(String jobName) {
		SchedulerJobInfo jobInfo = new SchedulerJobInfo();
		try {
			jobInfo.setJobName(jobName);
			jobInfo = schedulerRepository.findByJobName(jobName);
			schedulerRepository.delete(jobInfo);
			log.info(">>>>> jobName = [" + jobName + "]" + " deleted.");
			 schedulerFactoryBean.getScheduler()
					.deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
		} catch (SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
		}
	}
	
	public void deleteJobByGroupName(String userName) {

		List<SchedulerJobInfo> allJobsByGroup = schedulerRepository.findAllByJobGroup(userName);
		for (SchedulerJobInfo jobInfo : allJobsByGroup) {
			try {
				schedulerRepository.deleteById(jobInfo.getJobId());
				log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
				schedulerFactoryBean.getScheduler()
						.deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			} catch (SchedulerException e) {
				log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			}
		}
	}

	public void scheduleAllJobs(String userName) throws Exception {

		List<SchedulerJobInfo> jobInfoList = SequenceAndSchedulerJobCreator.createJobObjects(userName);
			
		for(SchedulerJobInfo jobInfo:jobInfoList) {
			
			log.info("Job Info: {}", jobInfo);
			scheduleNewJob(jobInfo);
			jobInfo.setDesc("Automated Message Sending Job " + jobInfo.getJobId());
			jobInfo.setInterfaceName("interface_" + jobInfo.getJobId());
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " created.");
			
		}
	}

	@SuppressWarnings("unchecked")
	private void scheduleNewJob(SchedulerJobInfo jobInfo) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			JobDetail jobDetail = JobBuilder
					.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {

				jobDetail = scheduleCreator.createJob(
						(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
						jobInfo.getJobName(), jobInfo.getJobGroup());
				jobDetail.getJobDataMap().put("sequenceObject", jobInfo.getSequenceDto());
				Trigger trigger;
				
					trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), jobInfo.getStartTime(),
							jobInfo.getSequenceDto().getDelayTimeForAutomatedMessage(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
					
				scheduler.getListenerManager().addJobListener(new JobEventListener());
				scheduler.scheduleJob(jobDetail, trigger);
				jobInfo.setJobStatus("SCHEDULED");
				schedulerRepository.save(jobInfo);
				log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
			} else {
				log.error("scheduleNewJobRequest.jobAlreadyExist");
			}
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	

}
