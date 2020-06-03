/******************************************************************************
 1 *  Compilation:  javac -d bin Response.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create  Usertest  for  write test case for user 
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bridgelabz.dto.Logindto;
import com.bridgelabz.dto.Registerdto;
import com.bridgelabz.dto.Setpassworddto;
import com.bridgelabz.model.Rabbitmqmodel;
import com.bridgelabz.model.User;
import com.bridgelabz.repo.Userrepo;
import com.bridgelabz.response.Response;
import com.bridgelabz.services.ServiceImp;
import com.bridgelabz.utility.Tokenutility;
import com.bridgelabz.utility.Utility;

@RunWith(SpringJUnit4ClassRunner.class)   //    runner built into JUnit  

@SpringBootTest
public class Usertest {

	@Mock
	private Userrepo repo;   //create  user  repo object using mock duplicate
	@InjectMocks
	private ServiceImp serviceImp;  //create user serviceimp object using mock duplicate
	@Mock
	private ModelMapper mapper;  // create modelmapper object
	@Mock
	private Tokenutility tokenutility; //create object of  tokenutility user token  create and gettoken	
	@Mock
	private Utility utility;	 //create object  for user  rabbitmq
	@Mock
	private JavaMailSender javaMailSender;	 //create object of  javamailsender
	@Mock
	private RabbitTemplate template;	  // create object of  rabbitemplate
	@Mock
    private PasswordEncoder passwordEncoder;  // create object of  passwordencoder

	String userid = "5dddff500ed0504ee89964f0";
	String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZGU4OWQwZDE3ZjBlMTcyNDJiMDQzMjciLCJpYXQiOjE1NzU1MjU2NDV9.LruI0fUkD23CDeZLL9TTOS4pffXwIqVP20UL1iFMP98";
	String idd = "1";
	String emailid = "panditwalde64@gmail.com";
	User user = new User();
	User updateuser = new User();
	Logindto logindto = new Logindto();
	Registerdto registerdto=new Registerdto();
	Setpassworddto setpassworddto=new Setpassworddto();	
	Optional<User> id = Optional.of(user);
	Rabbitmqmodel rabbitMqModel=new Rabbitmqmodel();
	User user1=new User();

	
	/**
	 *   purpose - write test case for show all user infomation using junit and mokito
	 */
	@Test
	public void showAlluser() {

		user.setId(idd);
		user.setFirstname("pandit");
		user.setLastname("walde");
		user.setEmail(emailid);
		user.setPassword("abcd");
		user.setProfile("dggdgg");
		user.setValidate(true);
		User user1 = new User();
		user1.setId(idd);
		user1.setFirstname("ajay");
		user1.setLastname("lodale");
		user1.setEmail(emailid);
		user1.setPassword("abdggfdfgcd");
		user1.setProfile("abc.png");
		user1.setValidate(true);
		ArrayList<User> list = new ArrayList<User>();
		list.add(user);
		list.add(user1);

		when(repo.findAll()).thenReturn(list);   //this method return found all user result in  list
		List<User> listofuser = serviceImp.Show(token);   //listofuser  return listof user
		assertEquals("pandit", listofuser.get(0).getFirstname());   //Asserts that expected and actual are equal &

	}

	@Test
	public void addUser() {
		
	    registerdto.setFirstname("pandit");
	    registerdto.setLastname("walde");
	    registerdto.setEmail("panditwalde64@gmail.com");
	    registerdto.setPhonenumber(95613184);
	    registerdto.setPassword("Pass@123");	    
		when(mapper.map(registerdto,User.class)).thenReturn(user);
		System.out.println(user);
//		when(repo.findAll().stream().anyMatch(i ->i.getEmail().equals(registerdto.getEmail())));		 
		when(passwordEncoder.encode(registerdto.getPassword())).thenReturn(Mockito.anyString());		 
		user.setPassword(registerdto.getPassword());		
		when(tokenutility.createToken(userid)).thenReturn(token);		
		when(utility.getRabbitMq(emailid, token)).thenReturn(rabbitMqModel);
		template.convertAndSend("userMessageQueue", rabbitMqModel);		
//		javaMailSender.send(Utility.getRabbitMq(registerdto.getEmail(), token));		
		Response res=serviceImp.addNewUser(registerdto);
		assertEquals(200, res.getStatus());
		
	}

	/**
	 *  purpose:  write test case  for  user login using  junit mokito 
	 */
	@Test
	public void loginUser() {

		when(repo.findByEmail(logindto.getEmail())).thenReturn(user);    //this method  if user found return it or not
		when(tokenutility.createToken(idd)).thenReturn(token);           //this method create token and return it
		Response res = serviceImp.loginUser(logindto);                   //this method return response of  service loginuser 
		assertEquals(200, res.getStatus());                              //this method return response status

	}

	@Test
	public void findByUser() {

		when(tokenutility.getUserToken(token)).thenReturn(userid);
		when(repo.findById(userid)).thenReturn(id);
		Response res = serviceImp.findByUser(token);
		assertEquals(200, res.getStatus());
	}

	@Test
	public void deleteUser() {

		when(tokenutility.getUserToken(token)).thenReturn(userid);
		when(repo.findById(userid)).thenReturn(id);     
		Response res = serviceImp.deleteUser(token);
		assertEquals(200, res);

	}

	@Test
	public void updateuser() {
		when(tokenutility.getUserToken(token)).thenReturn(userid);
		when(repo.findById(userid).get()).thenReturn(updateuser);
		updateuser = user;
		when(repo.save(updateuser)).thenReturn(updateuser);
		Response res = serviceImp.updateuser(user, token);
		assertEquals(200, res.getStatus());

	}

	@Test
	public void findEmail() {

	}

	@Test
	public void setPassword() {
		when(tokenutility.getUserToken(token)).thenReturn(userid);
		System.out.println(userid);
		when(repo.findById(userid).get().getEmail()).thenReturn(emailid);
		System.out.println(emailid);
		when(repo.findByEmail(emailid)).thenReturn(updateuser);
		updateuser.setPassword(passwordEncoder.encode(setpassworddto.getPassword()));
	}

	@Test
	public void addProfile() {
		
		
		

	}

}
