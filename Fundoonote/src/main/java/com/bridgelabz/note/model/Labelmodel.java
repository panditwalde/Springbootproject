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
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection =   "label")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Labelmodel {
	
	@Id
	private String id;
	private String lable_title;
	private LocalDateTime created_date= LocalDateTime.now();
	private LocalDateTime updated_date;	
	private String userid;     //add db in user id
	private String noteid;

	
	
	@DBRef(lazy = true)
	@JsonIgnore
	List<Notemodel> listOfNote = new ArrayList<Notemodel>();
	//create this list for many to many relationship between two class
	@Override
	public String toString() {
		return "Labelmodel [label_id=" + id + ", lable_title=" + lable_title + ", created_date=" + created_date
				+ ", updated_date=" + updated_date + ", user_id=" + userid + "]";
	}
	

}
