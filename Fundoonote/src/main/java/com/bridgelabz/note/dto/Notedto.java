/******************************************************************************
 *  Compilation:  javac -d bin Tokenutility.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create utility for jwt  response in  token
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.dto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bridgelabz.note.model.Labelmodel;

import lombok.Data;

@Data   //genrate setter getter automically
public class Notedto {
	
	

	
	@NotBlank(message = "title is empty")	
	@Size(min = 3,max = 50,message = "Title should be statring  3 character")
	private String title;          //  getter setter of note title
	@NotBlank(message = "description is empty")	
	@Size(min = 3,max = 1000,message = "description should be statring  3 character")
	private String  description;   // getter setter of note  description
	private String color;
	private boolean pin;           // getter setter of pin 
	private boolean trash;         // getter setter of trash 
	private boolean archive;       // getter setter of archive 
	private String reminder;       // getter setter of remider 
	private List<String> collabrators=new ArrayList<String>();
	LocalDateTime datetime = LocalDateTime.now();
    private	List<Labelmodel> listOfLabels=new ArrayList<Labelmodel>(); //this list use for relationship between note and label


}
