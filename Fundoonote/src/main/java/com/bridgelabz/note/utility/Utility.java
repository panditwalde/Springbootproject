/******************************************************************************
 *  Compilation:  javac -d bin Response.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create  utility for  simple mail sending 
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.utility;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.bridgelabz.note.model.Rabbitmqmodel;





@Component
public class Utility {

	/**
	 * Purpose:this method can be create object of simple mail message and return it
	 * 
	 * @param email user give email for checking
	 * @return simple email message object
	 */
	public static SimpleMailMessage verifyUserMail(String email,String token,String link) {
		
	
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);     //send mail
	
		msg.setSubject("test"); //send message for user email account
		msg.setText("hello"+(link+token));  //send token for  user email  account
		System.out.println("in simple mail :"+ email);
		System.out.println(msg.getText());
		return msg;

	}
	
	
	public static Rabbitmqmodel  getRabbitMq(String email ,String token) {
		
		Rabbitmqmodel rm=new Rabbitmqmodel();
		rm.setBody("invite");
		rm.setEmail(email);
		rm.setSubject("Verfication link");
		return rm;
		
		
		
	}
	
	

}
