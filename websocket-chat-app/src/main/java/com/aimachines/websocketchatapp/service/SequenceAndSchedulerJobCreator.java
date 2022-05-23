package com.aimachines.websocketchatapp.service;

import java.util.ArrayList;
import java.util.List;

import com.aimachines.websocketchatapp.job.AutomaticMessagingJob;
import com.aimachines.websocketchatapp.model.SchedulerJobInfo;
import com.aimachines.websocketchatapp.model.SequenceDTO;

public class SequenceAndSchedulerJobCreator {
	
	
	public static String S1 = "{\"sendmessage\", \"EmployeeA\", \"John Doe\",,  email, Template1}";
	
	
	public static List<SchedulerJobInfo> createJobObjects(String jobName){
		 List<SequenceDTO> sequences = fetchSequences(jobName);
		List<SchedulerJobInfo> jobInfoList = new ArrayList();
		int index =0;
		for(SequenceDTO sequence : sequences ) {
			SchedulerJobInfo jobInfo = new SchedulerJobInfo();
			index +=1;
			jobInfo.setJobName(jobName+"S"+index);
			jobInfo.setJobGroup(jobName);
			jobInfo.setStartTime(System.currentTimeMillis());
			jobInfo.setFireTime(sequence.getDelayTimeForAutomatedMessage());
			jobInfo.setJobClass(AutomaticMessagingJob.class.getName());
			jobInfo.setSequenceDto(sequence);
			jobInfoList.add(jobInfo);
		}
		return jobInfoList;
	}
	
	
	public static List<SequenceDTO> fetchSequences(String userName){
		
		List <SequenceDTO> sequenceList = new ArrayList<>();
		int temp = 0;
		long time= 60000;//settings for 1 seconds
		
		for(int i=0 ;i<3 ; i++) {
			temp+=1;
			SequenceDTO sequence = new SequenceDTO();
			sequence.setEmployeeName("Sales Agent "+temp);
			sequence.setJobType("sendMessage");
			sequence.setMessageMedium("Email");
			sequence.setMessageTemplate("messagetemplate"+temp);
			sequence.setDelayTimeForAutomatedMessage((time*temp));
			sequence.setToUser(userName);
			sequenceList.add(sequence);
		}
		
		return sequenceList;
	}
}
