package com.bridgelabz.dto;

import org.springframework.stereotype.Component;

import com.bridgelabz.model.User;

import lombok.Data;
@Data
@Component
public class LoginresponseDto {
	
	private User user;
	private String token;

}
