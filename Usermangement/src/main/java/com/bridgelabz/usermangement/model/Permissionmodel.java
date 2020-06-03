package com.bridgelabz.usermangement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Document(collection = "user_permission")

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Permissionmodel {

	@Id
	private String id;
	private String userid;
	private String menuid;
	private int add ;
	private int delete;
	private int modify ;
	private int read ;

}
