package com.aimachines.websocketchatapp.service;

import java.util.ArrayList;
import java.util.List;

import com.aimachines.websocketchatapp.enums.JobType;
import com.aimachines.websocketchatapp.job.AutomaticMessagingJob;
import com.aimachines.websocketchatapp.model.SchedulerJobInfo;
import com.aimachines.websocketchatapp.model.SequenceDTO;


public class SequenceAndSchedulerJobCreator {
	
	
	public static List<SchedulerJobInfo> createJobObjects(String jobName, JobType typeOfJob) {

		if (null == typeOfJob) {
			typeOfJob = JobType.DEFAULT;
		}

		List<SequenceDTO> sequences = null;
		// checking if type of job
		String jobTypes= null;
		if (typeOfJob.equals(JobType.SALES)) {
			sequences = fetchSalesSequences(jobName);
			jobTypes = "sales";
		} else if (typeOfJob.equals(JobType.SERVICE)) {
			sequences = fetchServiceSequences(jobName);
			jobTypes = "service";
		} else if (typeOfJob.equals(JobType.DEFAULT)){
			sequences = fetchDefaultSequences(jobName);
			jobTypes="def";
		}

		List<SchedulerJobInfo> jobInfoList = new ArrayList();
		int index = 0;
		for (SequenceDTO sequence : sequences) {
			SchedulerJobInfo jobInfo = new SchedulerJobInfo();
			index += 1;
			jobInfo.setJobName(jobName + jobTypes + index);
			jobInfo.setJobGroup(jobName);
			jobInfo.setStartTime(System.currentTimeMillis());
			jobInfo.setFireTime(sequence.getDelayTimeForAutomatedMessage());
			jobInfo.setJobClass(AutomaticMessagingJob.class.getName());
			jobInfo.setSequenceDto(sequence);
			jobInfoList.add(jobInfo);
		}
		return jobInfoList;
	}
	
	
	public static List<SequenceDTO> fetchDefaultSequences(String userName){
		
		List <SequenceDTO> sequenceList = new ArrayList<>();
		int temp = 0;
		long time= 60000;//settings for 1 min
		
		for(int i=0 ;i<3 ; i++) {
			temp+=1;
			SequenceDTO sequence = new SequenceDTO();
			sequence.setEmployeeName("Sally");
			sequence.setMessagingType("sendMessage");
			sequence.setMessageMedium("Email");
			sequence.setMessageTemplate("default"+temp);
			sequence.setDelayTimeForAutomatedMessage((time*temp));
			sequence.setToUser(userName);
			sequence.setJobType(JobType.DEFAULT);
			sequenceList.add(sequence);
		}
		
		return sequenceList;
	}
	
public static List<SequenceDTO> fetchSalesSequences(String userName){
		
		List <SequenceDTO> sequenceList = new ArrayList<>();
		int temp = 0;
		long time= 60000;
		
		for(int i=0 ;i<3 ; i++) {
			temp+=1;
			SequenceDTO sequence = new SequenceDTO();
			sequence.setEmployeeName("Sales Agent");
			sequence.setMessagingType("sendMessage");
			sequence.setMessageMedium("Email");
			sequence.setMessageTemplate("sales"+temp);
			sequence.setDelayTimeForAutomatedMessage((time*i)+1000);
			sequence.setToUser(userName);
			sequence.setJobType(JobType.SALES);
			sequenceList.add(sequence);
		}
		
		return sequenceList;
	}
public static List<SequenceDTO> fetchServiceSequences(String userName){
	
	List <SequenceDTO> sequenceList = new ArrayList<>();
	int temp = 0;
	long time= 60000;//settings for 1 min
	
	for(int i=0 ;i<3 ; i++) {
		temp+=1;
		SequenceDTO sequence = new SequenceDTO();
		sequence.setEmployeeName("Service Agent");
		sequence.setMessagingType("sendMessage");
		sequence.setMessageMedium("Email");
		sequence.setMessageTemplate("service"+temp);
		sequence.setDelayTimeForAutomatedMessage((time*i)+1000);
		sequence.setToUser(userName);
		sequence.setJobType(JobType.SERVICE);
		sequenceList.add(sequence);
	}
	
	return sequenceList;
}
}
