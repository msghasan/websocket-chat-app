package com.aimachines.websocketchatapp.constants;

import java.lang.reflect.Field;

public class ConstantMessages {

	public ConstantMessages() {
		super();
	}
	
	
	public static String default1="Hi ${message.username} Welcome to AI machines. I am your assistant today. How can I assist you?";
	public static String default2=" ${message.username} State your problem Sir, so I can assist you better.";
	public static String default3=" ${message.username} Please reply back or else this session will be over if you do not respond in next few minute";
	
	public static String sales1="Hi ${message.username} I will help you with your sales and purchase requirement";
	public static String sales2=" ${message.username} Please reply me what you want to buy or sell";
	public static String sales3=" ${message.username} Please reply back or else this session will be over for sales";

	public 	static String service1="Hi ${message.username} I will help you with your service requirement";
	public 	static String service2=" ${message.username} Please reply me what services you need";
	public 	static String service3=" ${message.username} Please reply back or else this session will be over for service";
	
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		 ConstantMessages cm  = new ConstantMessages();
		 Field fieldName = ConstantMessages.class.getField("default1");
		 Field[] fields = ConstantMessages.class.getFields();
		 for(Field field:fields) {
			 System.out.print(field.getName()+" : ");
			 System.out.println(ConstantMessages.class.getField(field.getName()));
		 }
		 
	}

}

