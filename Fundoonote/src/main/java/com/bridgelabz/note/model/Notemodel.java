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
package com.bridgelabz.note.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * purpose  create  note model
 *
 */
@Document(collection = "note")

@Data
public class Notemodel {
	
	
	@Id
	private String id;  	
	private String title;  
	private String  description;
	private String color;
	private boolean pin;
	private boolean trash;
	private boolean archive;
	private String reminder;	
	private LocalDateTime date = LocalDateTime.now();	
	private String userid;
	private List<String> collabrators=new ArrayList<String>();
 //list use for collaborators other person
//	@JsonIgnore
	
	@DBRef
	List<Labelmodel> listOfLabels=new ArrayList<Labelmodel>(); //this list use for relationship between note and label
	


	@Override
	public String toString() {
		return "Notemodel [id=" + id + ", title=" + title + ", description=" + description + ", color=" + color
				+ ", pin=" + pin + ", trash=" + trash + ", archive=" + archive + ", reminder=" + reminder
				+  ", date=" + date + "]";
	}
	
	
	
	

}
