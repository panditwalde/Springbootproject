/******************************************************************************
 *  Compilation:  javac -d bin LabelserviceImp.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create serviceimp class for write all logic of label
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.note.dto.Labeldto;
import com.bridgelabz.note.exception.custom.Labelnotfoundexception;
import com.bridgelabz.note.exception.custom.Notenotfoundexception;
import com.bridgelabz.note.exception.custom.Tokenexception;
import com.bridgelabz.note.exception.custom.Usernotfoundexception;
import com.bridgelabz.note.model.Labelmodel;
import com.bridgelabz.note.model.Notemodel;
import com.bridgelabz.note.repo.Labelrepository;
import com.bridgelabz.note.repo.Noterepository;
import com.bridgelabz.note.response.Response;
import com.bridgelabz.note.utility.Tokenutility;

/**
 * @author user
 *
 */
@Component

public class LabelserviceImp implements Labelservice {
	static Logger logger = LoggerFactory.getLogger(LabelserviceImp.class);

	@Autowired
	private ModelMapper mapper; // create modelmapper object
	@Autowired
	private Labelrepository labelRepo; // create lable repository object
	@Autowired
	private Noterepository noteRepo; // create Noterepository object
	@Autowired
	private Tokenutility tokenUtility; // create Tokenutility object

	/**
	 * purpose add new user label
	 * 
	 * @return
	 */

//	@Cacheable(value = "labeldto", key = "#token")
	@Override
	public Response labelAdd(String labeldto, String token) {

		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Labelmodel labelmodel = mapper.map(labeldto, Labelmodel.class);

		labelmodel.setLable_title(labeldto);
		labelmodel.setUserid(userid);

		labelRepo.save(labelmodel);
		return new Response(200, MessageReference.LABEL_ADD_SUCCESSFULLY, true);

	}

	public Response labelAddWithNote(Labeldto labeldto, String token) {

		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Labelmodel labelmodel = new Labelmodel();

		labelmodel.setLable_title(labeldto.getLable_title());
		;
		labelmodel.setNoteid(labeldto.getNoteid());
		labelmodel.setUserid(userid);
		
	
	   Labelmodel  al= 	labelRepo.save(labelmodel);
	        
	      manyToMany(labeldto.getNoteid(), al.getId(), token);
	      

//	      
	    
		return new Response(200, MessageReference.LABEL_ADD_SUCCESSFULLY, true);

	}

	/**
	 * purpose delete perticular label
	 */

	@Override
	public Response labelDelete(String labelid, String token) {

		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		Optional<Labelmodel> id = labelRepo.findByIdAndUserid(labelid, userid);
		if (id.isEmpty()) {
			throw new Labelnotfoundexception(MessageReference.LABEL_NOT_FOUND);
		}
		labelRepo.deleteById(labelid);
		return new Response(200, "label delete", MessageReference.LABEL_DELETE_SUCCESSFULLY);

	}

	/**
	 * purpose update perticular label
	 */
	@Override
	public Response labelUpdate(String label_title, String labelid, String token) {

		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		Optional<Labelmodel> id = labelRepo.findByIdAndUserid(labelid, userid);

		if (id.isEmpty()) {
			throw new Labelnotfoundexception(MessageReference.LABEL_NOT_FOUND);
		}
		Labelmodel labelmodel = id.get();
		labelmodel.setLable_title(label_title);
		LocalDateTime datetime = LocalDateTime.now();
		labelmodel.setUpdated_date(datetime);

		labelRepo.save(labelmodel);

		return new Response(200, "label update", MessageReference.LABEL_UPDATE_SUCCESSFULLY);

	}

	/**
	 * purpose show all user label
	 */
	@Override
	public ArrayList<Labelmodel> labelShowAll(String token) {

		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		return (ArrayList<Labelmodel>) labelRepo.findByUserid(userid);

	}

	/**
	 * purpose Search a perticular user label
	 */
	@Override
	public Response labelSearch(String labelid, String token) {
		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		Optional<Labelmodel> id = labelRepo.findByIdAndUserid(labelid, userid);
		if (id.isEmpty()) {
			throw new Labelnotfoundexception(MessageReference.LABEL_NOT_FOUND);
		}
		return new Response(200, "  user label ", labelRepo.findById(id));
	}

	/**
	 * purpose find by user id for label
	 */
	@Override
	public Response findLabelByUser_id(String user_id, String token) {
		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		return new Response(200, "  user label ", labelRepo.findByUserid(userid));
	}

	/**
	 * purpose main goal is relationship between label and note
	 */
	@Override
	public Response manyToMany(String noteid, String labelid, String token) {
		
	
		String userid = tokenUtility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		List<Notemodel> notelist = new ArrayList<Notemodel>();
		List<Labelmodel> labellist = new ArrayList<Labelmodel>();
		Optional<Labelmodel> userlabelid = labelRepo.findById(labelid); // check label id present or not
		if (userlabelid.isEmpty()) {
			throw new Notenotfoundexception(MessageReference.NOTE_NOT_FOUND);
		}
		Optional<Notemodel> usernoteid = noteRepo.findById(noteid); // check note id present or not
		if (usernoteid.isEmpty()) {
			throw new Labelnotfoundexception(MessageReference.LABEL_NOT_FOUND);
		}
		Labelmodel label = userlabelid.get();
		Notemodel note = usernoteid.get();
		
		
		notelist = label.getListOfNote();
		labellist = note.getListOfLabels();
		
		labellist.add(label);
		notelist.add(note);
		label.setListOfNote(notelist);
		note.setListOfLabels(labellist);
		labelRepo.save(label);
		noteRepo.save(note);

		return new Response(200, "assign note", " mapping sucess");

	}
	
	       

	
	@Override
	public Response noteWithLabel(String labelid, String token) {

		String userid = tokenUtility.getUserToken(token);
		Optional<Labelmodel> id = labelRepo.findByIdAndUserid(labelid, userid);
		System.out.println(id.get());
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.NOTE_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		}
		

		List<Notemodel> listOfNote = new ArrayList<Notemodel>();
		
		String noteId = id.get().getNoteid();

		  if(noteId.isEmpty()) {
			  return new Response(400, "label with note not found", null);
		  } 
		
			 System.out.println("1");

		listOfNote.add(noteRepo.findById(noteId).get());

		return new Response(200, "label with note", listOfNote);
	}
	
	public Response labelwithnote(String labelid,String noteid,String token) {
		
		String userid = tokenUtility.getUserToken(token);
		return new Response(200, "label with note", labelRepo.findByIdAndNoteidAndUserid(labelid, noteid, userid));

	}
	
public Response Updatelabelwithnote(String labelid,String noteid,String token) {
		
		String userid = tokenUtility.getUserToken(token);
		return new Response(200, "label with note", labelRepo.findByIdAndNoteidAndUserid(labelid, noteid, userid));

	}
	
	

}
