package com.aimachines.websocketchatapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aimachines.websocketchatapp.model.SchedulerJobInfo;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

	SchedulerJobInfo findByJobName(String jobName);
	List<SchedulerJobInfo>  findAllByJobGroup(String groupName);
}
