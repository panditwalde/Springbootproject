package com.bridgelabz.usermangement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Userdto {

	@NotEmpty(message = "First Name is mandatory")
	@Pattern(regexp = "^[a-zA-Z]*$")
	private String firstName;
	@NotBlank(message = "Middle is mandatory")
	@Pattern(regexp = "^[a-zA-Z]*$")
	private String middleName;
	@NotBlank(message = "Last Name is mandatory")
	@Pattern(regexp = "^[a-zA-Z]*$")
	private String lastname;
	private String dateOfBirth;
	private String gender;
	private String country;
	private String phoneNumber;
	@NotBlank(message = "Phone is mandatory")

	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\. [A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "phone format wrong")
	private String email;
	private String address;
	private String password;
	private String confirmPassword;
	private String username;
	private String userRole;
	private boolean status;
	private String profilePicture;

}
