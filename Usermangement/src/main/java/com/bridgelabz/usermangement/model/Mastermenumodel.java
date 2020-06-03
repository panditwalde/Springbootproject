package com.bridgelabz.usermangement.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "mastermenu")
public class Mastermenumodel {		
	
	
	private String id;
    private String menuName;
    private LocalDateTime created_date= LocalDateTime.now();
	private LocalDateTime updated_date;	

}
