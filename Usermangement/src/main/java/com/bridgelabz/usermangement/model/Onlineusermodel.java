package com.bridgelabz.usermangement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Onlineusermodel {
	
	
	
	private String id;
	private String userid;
	private int usercount=0;

}
