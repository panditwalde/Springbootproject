package com.bridgelabz.usermangement.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Document(collection = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usermodel {

	@Id	
	private String id;
	private String firstName;
	private String middleName;
	private String lastname;
	private String dateOfBirth;
	private String gender;
	private String country;
	private String phoneNumber;
	private String email;
	private String address;
	private String password;
	private String username;
	private String userRole;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private boolean status;
	private String profilePicture;
	
	
}
