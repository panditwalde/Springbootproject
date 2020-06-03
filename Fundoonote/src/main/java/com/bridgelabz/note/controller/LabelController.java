/******************************************************************************
 *  Compilation:  javac -d bin LabelController.java
 *  Execution:      java -cp bin com.bridgelabz.Lebelserviceimp.labelAdd    labeldto , token
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.labelDelete    id
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.labelUpdate    id
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.labelSearch    id
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.labelShowAll   
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.findLabelByUser_id    id
 *                  java -cp bin com.bridgelabz.Lebelserviceimp.assignNote noteid,labelid    
 *                        
 *               
 *  
 *  Purpose:       create controller for   user label
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.controller;

import java.io.IOException;

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

import com.bridgelabz.note.dto.Labeldto;

import com.bridgelabz.note.response.Response;
import com.bridgelabz.note.services.LabelserviceImp;

/**
 * @author user
 *
 */
@RestController
@RequestMapping("/")
@CrossOrigin
public class LabelController {

	@Autowired
	LabelserviceImp lableServiceImp; // create object labelserviceImp class

	/**
	 * @param labelDto create label for user
	 * @param token    give user token
	 * @return if label add send response to user label add successfully
	 */
	@PostMapping("/addlabel")
	public ResponseEntity<Response> labelAdd(@Valid @RequestBody  String label, @RequestHeader String token) {
         
		return new ResponseEntity<Response>(lableServiceImp.labelAdd(label, token), HttpStatus.OK);

	}
	@PostMapping("/addlabelwithnote")
	public ResponseEntity<Response> labelAddWithNote(@Valid  @RequestBody Labeldto labeldto,@RequestHeader String token){
		       

		
		return new ResponseEntity<Response>(lableServiceImp.labelAddWithNote(labeldto, token), HttpStatus.OK);
	}

	/**
	 * @param id user provide id for which use user want to delete
	 * @return if label delete send response to user label delete successfully
	 */
	@DeleteMapping("/deletelabel")
	public ResponseEntity<Response> labelDelete(@Valid @RequestParam String labelid, @RequestHeader String token) {

		return new ResponseEntity<Response>(lableServiceImp.labelDelete(labelid, token), HttpStatus.OK);

	}

	/**
	 * @param labeldto update label for user
	 * @param id       user provide id for which use user want to update a record
	 * @return if label update send response to user label update successfully
	 */
	@PutMapping("/updatelabel")
	public ResponseEntity<Response> labelUpdate(@Valid @RequestParam String label_title, @RequestParam String label_id, @RequestHeader String token) {

		     

		return new ResponseEntity<Response>(lableServiceImp.labelUpdate(label_title, label_id, token), HttpStatus.OK);

	}

	/**
	 * @param id user provide id for which use user want to search record
	 * @return retrun the user found record
	 */
	@GetMapping("/searchlabel")
	public ResponseEntity<Response> searchLabel(@Valid @RequestParam String id, @RequestHeader String token) {
		return new ResponseEntity<Response>(lableServiceImp.labelSearch(id, token), HttpStatus.OK);

	}

	/**
	 * @return return all user details
	 */
	@GetMapping("/getalllabel")
	public Response showAllLabel(@RequestHeader String token) {
		   

		return new Response(200, " all lable ", lableServiceImp.labelShowAll(token));
	}

	/**
	 * @param check userid present or not in db
	 * @return if user id found return it
	 */
	@PostMapping("/findLabelbyuserid")
	public ResponseEntity<Response> findByUserid(@Valid @RequestParam String userid, @RequestHeader String token) {

		return new ResponseEntity<Response>(lableServiceImp.findLabelByUser_id(userid, token), HttpStatus.OK);

	}

	/**
	 * @param noteid  user provide noteid for mapping
	 * @param labelid user provide labelid for mapping
	 * @return if assign note to label return sucess message
	 */

	@PutMapping("/UpdatelabelWithNotes")
	public ResponseEntity<Response> UpdatelabelWithNotes(@Valid @RequestParam String noteid, @RequestParam String labelid , @RequestParam String token) {

		System.out.println("noteid11"+noteid);
		System.out.println("labelid11"+labelid);
		System.out.println("token11"+token);

		
		return new ResponseEntity<Response>(lableServiceImp.manyToMany(noteid, labelid, token), HttpStatus.OK);

	}
	@GetMapping("/labelwithnote")
	public ResponseEntity<Response> labelWithNote(@RequestParam String labelid,   @RequestHeader String token) throws IOException {

		
		return new ResponseEntity<Response>(lableServiceImp.noteWithLabel(labelid,  token), HttpStatus.OK);
	}
	
	@GetMapping("/labelwithnotes")
	public ResponseEntity<Response> labelWithNotes(@RequestParam String labelid, @RequestParam String noteid,   @RequestHeader String token) throws IOException {

		return new ResponseEntity<Response>(lableServiceImp.labelwithnote(labelid, noteid,token), HttpStatus.OK);
	}

}
