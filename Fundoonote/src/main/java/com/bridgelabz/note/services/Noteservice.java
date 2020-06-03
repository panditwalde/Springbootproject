/******************************************************************************
 *  Compilation:  javac -d bin Tokenutility.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create interface for   user note
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  25-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


import com.bridgelabz.note.dto.Collabratordto;
import com.bridgelabz.note.dto.Notedto;
import com.bridgelabz.note.model.Notemodel;
import com.bridgelabz.note.response.Response;

public interface Noteservice {
	
	
	
	public Response createNote(Notedto notedto,String token) throws IOException; //create createNote() method for add new note
	public Response deletePermanentNote(String id ,String token) throws IOException;                  //create deleteNote() method for delete note
	public Response UpdateNote(Notedto notedto,String id ,String token) throws IOException;    //create UpdateNote() method for update note 
	public Response searchNote(String id ,String token);     //create UpdateNote() method for search perticular note
    public List<Notemodel>  showAllNote(String token);                 //create showAllNote() method for show all note
    public Response sortNoteByName(String token);              //create sortNoteByName() method for sort note by name
	public Response sortNoteByDate(String token);             //create sortNoteByDate() method for sort note by date
	public Response addCollabrator(String collabratoremail,String noteid ,String token);   //create addCollabrator() method for collabrator other user
	public Response removeCollabrator(String email ,String noteid,String token);
	public Response archive(String token ,String noteid) throws IOException;    //create archive() method for user archive note
	public Response pin(String token ,String noteid) throws IOException;        //create UpdateNote() method for user archive pin 
	public Response trash(String noteid ,String token) throws IOException;       //create UpdateNote() method for  user archive trash
    public Response addReminder(String date,String noteid ,String token) throws IOException, ParseException; // create method for  add reminder
    public Response removeReminder(String noteid ,String token) throws IOException; // create method for remove reminder
	public Response setColor(String noteid ,String colorcode,String token) throws IOException; 

}
