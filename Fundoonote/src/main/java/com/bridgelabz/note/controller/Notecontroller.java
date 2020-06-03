/******************************************************************************
 *  Compilation:    javac -d bin Notecontroller.java
 *  Execution:      java -cp bin com.bridgelabz.Noteserviceimp.createNote    notedto , token
 *                  java -cp bin com.bridgelabz.Noteserviceimp.deleteNote    id
 *                  java -cp bin com.bridgelabz.Noteserviceimp.searchNote    id
 *                  java -cp bin com.bridgelabz.Noteserviceimp.UpdateNote    notedto  id
 *                  java -cp bin com.bridgelabz.Noteserviceimp.showAllNote   
 *                  java -cp bin com.bridgelabz.Noteserviceimp.sortNoteByName   
 *                  java -cp bin com.bridgelabz.Noteserviceimp.sortNoteByDate    
 *                  java -cp bin com.bridgelabz.Noteserviceimp.addCollabrator collabratorDto    
 *               
 *  
 *  Purpose:       create controller for  user note
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.note.dto.Collabratordto;
import com.bridgelabz.note.dto.Notedto;
import com.bridgelabz.note.response.Response;
import com.bridgelabz.note.services.ElasticsearchserviceImp;
import com.bridgelabz.note.services.NoteserviceImp;

@RestController
@RequestMapping("/")
@CrossOrigin
public class Notecontroller {

	@Autowired
	private NoteserviceImp noteServiceImp;

	@Autowired
	private ElasticsearchserviceImp elasticsearchserviceImp;

	/**
	 * @param notedto create note for user
	 * @param token   user token
	 * @return if note is create return add note successfully
	 * @throws IOException
	 */
	@PostMapping("/createnote")
	public ResponseEntity<Response> createNote(@Valid @RequestBody Notedto notedto, @RequestHeader String token)
			throws IOException {
		return new ResponseEntity<Response>(noteServiceImp.createNote(notedto, token), HttpStatus.OK);

	}

	/**
	 * @param id which note you want delete it
	 * @return if note delete return delete successfully.
	 * @throws IOException 
	 */
	@DeleteMapping("/deletenote")
	public ResponseEntity<Response> deleteNote(@RequestParam String noteid, @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.deletePermanentNote(noteid, token), HttpStatus.OK);

	}

	/**
	 * @param id search the particular note in db through by id
	 * @return if record is found return it.
	 */
	@GetMapping("/searchnote11")
	public ResponseEntity<Response> searchNote1(@Valid @RequestParam String id, @RequestParam String token) {

		return new ResponseEntity<Response>(noteServiceImp.searchNote(id, token), HttpStatus.OK);

	}

	/**
	 * @return return all note for user
	 */

	@GetMapping("/getallnote")

	public Response showAllNote(@RequestHeader String token) {
		   
		return new Response(200, "show all note", noteServiceImp.showAllNote(token));

	}

	/**
	 * @param notedto user provide to updated detail to controller
	 * @param id      user want which note to update a details
	 * @return if note it update return update successfully.
	 * @throws IOException 
	 */
	@PutMapping("/editnote")
	public ResponseEntity<Response> updateNote(@Valid @RequestBody Notedto notedto, @RequestParam String noteid,
			@RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.UpdateNote(notedto, noteid, token), HttpStatus.OK);

	}

	/**
	 * @return return all sorted note by date
	 */
	@GetMapping("/sortnotebyname")
	public ResponseEntity<Response> sortNoteByName(@RequestHeader String token) {

		return new ResponseEntity<Response>(noteServiceImp.sortNoteByName(token), HttpStatus.OK);

	}

	/**
	 * @return return all sorted note by name
	 */
	@GetMapping("/sortnotebydate")
	public ResponseEntity<Response> sortNoteByDate(@RequestHeader String token) {

		return new ResponseEntity<Response>(noteServiceImp.sortNoteByDate(token), HttpStatus.OK);

	}

	/**
	 * @param collabratorDto provide details which want to collaborate
	 * @return if other user is collaborate return add collabrator successfully
	 */
	@PutMapping("/addCollabrator")
	public ResponseEntity<Response> addCollbrator(@Valid @RequestParam String collabraotoremail,@RequestParam String noteid,
			@RequestHeader String token) {
		
	

		return new ResponseEntity<Response>(noteServiceImp.addCollabrator(collabraotoremail,noteid, token), HttpStatus.OK);

	}

	@DeleteMapping("/removeCollabrator")
	public ResponseEntity<Response> removeCollabrator(@RequestParam String email,@RequestParam String noteid, @RequestHeader String token) {

		 
		return new ResponseEntity<Response>(noteServiceImp.removeCollabrator(email,noteid, token), HttpStatus.OK);
	}

	/**
	 * @param title search perticular tilte in elastic search
	 * @return if title is found return user note or not
	 * 
	 */

	@GetMapping("/searchnote")
	public ResponseEntity<Response> searchNote(@RequestHeader String note, @RequestHeader String token) throws Exception {

		return new ResponseEntity<Response>(elasticsearchserviceImp.searchByTitle(note), HttpStatus.OK);

	}

	/**
	 * @param description search perticular description in elastic search
	 * @return if description is found return user note or not
	 * @throws Exception
	 */
	@GetMapping("/searchbyDescription")
	public ResponseEntity<Response> searchdByDescription(@RequestParam String description) throws Exception {

		return new ResponseEntity<Response>(elasticsearchserviceImp.searchByDescription(description), HttpStatus.OK);

	}

	/**
	 * @param date   user provide date for stroing
	 * @param noteid which note want to remider it
	 * @return if reminder add return reminder add successfully or not
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@PutMapping("/addreminder")
	public ResponseEntity<Response> addReminder(@RequestParam String date, @RequestParam String noteid,
			@RequestHeader String token) throws IOException, ParseException {
		   

		return new ResponseEntity<Response>(noteServiceImp.addReminder(date, noteid, token), HttpStatus.OK);
	}

	/**
	 * @param noteid which note want delete a reminder
	 * @return if reminder remove return reminder delete successfully or not
	 * @throws IOException 
	 */
	@DeleteMapping("/removereminder")
	public ResponseEntity<Response> removeReminder(@RequestParam String noteid, @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.removeReminder(noteid, token), HttpStatus.OK);
	}

	@PutMapping("/pinunpin")
	public ResponseEntity<Response> pinUnpin(@RequestHeader String token, @RequestParam String noteid) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.pin(token, noteid), HttpStatus.OK);
	}

	@PutMapping("/trash")
	public ResponseEntity<Response> trash(@RequestParam String noteid, @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.trash(noteid, token), HttpStatus.OK);
	}

	@PutMapping("/archive")
	public ResponseEntity<Response> archvie(@RequestParam String noteid, @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.archive(noteid, token), HttpStatus.OK);
	}

	@PutMapping("/setcolor")
	public ResponseEntity<Response> setColor(@RequestHeader String noteid, @RequestHeader String colorCode,  @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(noteServiceImp.setColor(noteid, colorCode, token), HttpStatus.OK);
	}
	
	

	

}
