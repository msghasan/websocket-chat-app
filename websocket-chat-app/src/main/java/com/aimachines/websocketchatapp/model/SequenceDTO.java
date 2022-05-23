package com.aimachines.websocketchatapp.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter
@ToString
public class SequenceDTO implements Serializable {

	private static final long serialVersionUID = -7286823136006505780L;
	private String jobType;
	private String employeeName;
	private Long delayTimeForAutomatedMessage;
	private String messageMedium;
	private String messageTemplate;
	private String toUser;
}
