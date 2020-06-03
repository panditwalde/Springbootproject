/******************************************************************************
 *  Compilation:  javac -d bin LabelserviceImp.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create serviceimp class for write all logic of note
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.note.services;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bridgelabz.note.dto.Notedto;
import com.bridgelabz.note.exception.custom.Notenotfoundexception;
import com.bridgelabz.note.exception.custom.Tokenexception;
import com.bridgelabz.note.exception.custom.Usernotfoundexception;
import com.bridgelabz.note.model.Collabratormodel;
import com.bridgelabz.note.model.Notemodel;
import com.bridgelabz.note.model.Rabbitmqmodel;
import com.bridgelabz.note.repo.Collabratorrepository;
import com.bridgelabz.note.repo.Noterepository;
import com.bridgelabz.note.response.Response;
import com.bridgelabz.note.utility.Tokenutility;
import com.bridgelabz.note.utility.Utility;

@Service
@CacheConfig(cacheNames = "note")
public class NoteserviceImp implements Noteservice {
	static Logger logger = LoggerFactory.getLogger(LabelserviceImp.class);
	@Autowired
	private Noterepository repo; // create Noterepository object
	@Autowired
	private ModelMapper mapper; // create ModelMapper object
	@Autowired
	private Tokenutility tokenutility; // create Tokenutility object
	@Autowired
	private ElasticsearchserviceImp elasticsearchserviceImp;

	@Autowired
	private Collabratorrepository collabratorrepository;
	
	@Autowired
	private RabbitTemplate template;
	
	@Autowired
	private JavaMailSender javaMailSender; // use JavaMailSender class


	/**
	 * purpose add new user note
	 */
	/**
	 * @throws IOException
	 *
	 */
	@Override
//	@Cacheable(key = "#token")
	public Response createNote(Notedto notedto, String token) throws IOException {
		Notemodel notemodel = mapper.map(notedto, Notemodel.class);

		String user_id = tokenutility.getUserToken(token);

		notemodel.setUserid(user_id);

		repo.save(notemodel);

		elasticsearchserviceImp.createDocuemnt(notemodel);
		return new Response(200, "note add", MessageReference.NOTE_ADD_SUCCESSFULLY);
	}

	/**
	 * purpose delete perticular note
	 * 
	 * @throws IOException
	 */
	@Override
	@CacheEvict(key = "#token")
	public Response deletePermanentNote(String noteid, String token) throws IOException {
		String userid = tokenutility.getUserToken(token);

		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
//		Notemodel note_id = id.get();
		if (id.isEmpty()) {
			throw new Notenotfoundexception(MessageReference.NOTE_ID_NOT_FOUND);
		}
		repo.delete(id.get());
		elasticsearchserviceImp.deleteDocuemnt(noteid);

		return new Response(200, "note add", MessageReference.NOTE_DELETE_SUCCESSFULLY);

	}

	/**
	 * purpose Search a perticular user note
	 */
	@Override
	public Response searchNote(String noteid, String token) {

		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		return new Response(200, "Note search", repo.findByIdAndUserid(noteid, userid));

	}

	/**
	 * purpose show all user note
	 */
	@Override
	public List<Notemodel> showAllNote(String token) {

		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		List<Notemodel> list = repo.findByUserid(userid);
		return list;
	}

	/**
	 * purpose update perticular Note
	 * 
	 * @throws IOException
	 */
	@Override
	public Response UpdateNote(Notedto notedto, String noteid, String token) throws IOException {

		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		if (id.isEmpty()) {
			throw new Notenotfoundexception(MessageReference.NOTE_ID_NOT_FOUND);
		}
		Notemodel updateNote = id.get();
		;
		updateNote.setDescription(notedto.getDescription());
		updateNote.setTitle(notedto.getTitle());
		LocalDateTime datetime = LocalDateTime.now();
		updateNote.setDate(datetime);
		elasticsearchserviceImp.updateDocuemnt(updateNote, noteid);
		repo.save(updateNote);

		return new Response(200, "Note update", MessageReference.NOTE_UPDATE_SUCCESSFULLY);

	}

	/**
	 * sort note by name
	 */
	@Override
	public Response sortNoteByName(String token) {

		List<Notemodel> note = showAllNote(token);
		if (note.isEmpty()) {
			return new Response(200, "sort by name", MessageReference.NOTE_IS_EMPTY);
		}

		note.sort((note1, note2) -> note1.getTitle().compareTo(note2.getTitle()));

		return new Response(200, "Sort note by name", note);
	}

	/**
	 * sort note by date
	 */
	@Override
	public Response sortNoteByDate(String token) {

		List<Notemodel> note = showAllNote(token);
		if (note.isEmpty()) {
			return new Response(200, "sort by date", MessageReference.NOTE_IS_EMPTY);
		}
		note.sort((note1, note2) -> note1.getDate().compareTo(note2.getDate()));
		return new Response(200, "sort by date", note);
	}

	/**
	 * collabrator other user
	 * 
	 */

	@Override
	public Response addCollabrator(String collbratoremail, String noteid, String token) {

		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.NOTE_ID_NOT_FOUND);
		}
		Notemodel note = id.get();

		List<String> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
		list = note.getCollabrators();
		if (list == null) {
			list1.add(collbratoremail);
		} else {
			for (String s : list) {
				list1.add(s);
			}

			list1.add(collbratoremail);
		}
		note.setCollabrators(list1);
		collabratorList(note, collbratoremail, token);
		
		   
		
		
		repo.save(note);
		
		
	    Rabbitmqmodel body = Utility.getRabbitMq(collbratoremail, token);
		template.convertAndSend("userMessageQueue", body);
		javaMailSender.send(Utility.verifyUserMail(collbratoremail, token, MessageReference.Verfiy_MAIL_TEXT)); // send email

		return new Response(200, "add collbrator", "collabrator add successfully");

	}

	private void collabratorList(Notemodel note, String colaboratorId, String token) {
		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Collabratormodel> colab = collabratorrepository.findByColaboratorUsernameAndOwnerId(colaboratorId,
				userid);
		if (colab.isPresent()) {
			Collabratormodel col = colab.get();

			List<Notemodel> listOfNote = col.getCollabratorlist();
			listOfNote.add(note);
			col.setCollabratorlist(listOfNote);
			collabratorrepository.save(col);
		}

	}

	@Override
	public Response removeCollabrator(String email, String noteid, String token) {
		

		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		
		  
		    
		Optional<Notemodel> getNote1 = getNote(noteid, token);
		Notemodel note = getNote1.get();
		List<String> list = new ArrayList<String>();
		list = note.getCollabrators();
		list.remove(email);
		note.setCollabrators(list);
		repo.save(note);
		removegetCollabrator(note, email, token);

		return new Response(200, "collabrator remove succssfully", true);
	}

	private Optional<Notemodel> getNote(String noteid, String token) {
		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		
		     Optional<Notemodel> findnote=repo.findByIdAndUserid(noteid, userid);

		return findnote;
	}

	private void removegetCollabrator(Notemodel note, String email, String token) {
		String userid = tokenutility.getUserToken(token);
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		Optional<Collabratormodel> colab = collabratorrepository.findByColaboratorUsernameAndOwnerId(email, userid);

		if (colab.isPresent()) {
			Collabratormodel colaboratorObj = colab.get();
			List<Notemodel> listOfNote = colaboratorObj.getCollabratorlist();
			listOfNote.remove(note);
			if (listOfNote.size() != 0) {
				colaboratorObj.setCollabratorlist(listOfNote);
				collabratorrepository.save(colaboratorObj);
				return;
			} else {
				collabratorrepository.delete(colaboratorObj);
			}
		}

	}

	/**
	 * purpose create method for archive user note and label
	 * 
	 * @throws IOException
	 */
	@Override
	public Response archive(String noteid, String token) throws IOException {

		String userid = tokenutility.getUserToken(token); // if token is invalid then throw exception invalid token
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);

		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.USER_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		} else {
			Notemodel note = id.get();
			note.setArchive(!(note.isArchive()));
			elasticsearchserviceImp.updateDocuemnt(note, noteid);

			repo.save(note);
			return new Response(200, "archive change", true);

		}
	}

	/**
	 * purpose create method for pin unpin user note and label
	 * 
	 * @throws IOException
	 */
	@Override
	public Response pin(String token, String noteid) throws IOException {
		String userid = tokenutility.getUserToken(token); // if token is invalid then throw exception invalid token
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}

		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		Notemodel note = id.get(); // if userid not found throw exception user not found
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.USER_ID_NOT_FOUND);

		} else {
			note.setPin(!(note.isPin())); // if pin change if ture to false or false to ture
			repo.save(note); // store in db
			elasticsearchserviceImp.updateDocuemnt(note, noteid);
			return new Response(200, "pin change", true);

		}
	}

	/**
	 * purpose create method for trash store user note retrive and delete
	 * 
	 * @throws IOException
	 */
	@Override
	public Response trash(String noteid, String token) throws IOException {

		String userid = tokenutility.getUserToken(token); // if token is invalid then throw exception invalid token
		if (userid.isEmpty()) {
			throw new Tokenexception(MessageReference.INVALID_TOKEN);
		}
		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		Notemodel note = id.get();
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.USER_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		} else {
			note.setTrash(!(note.isTrash()));
			elasticsearchserviceImp.updateDocuemnt(note, noteid);

			repo.save(note);
			return new Response(200, "trash change", true);

		}
	}

	/**
	 * purpose create method for add reminder
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Override
	public Response addReminder(String date, String noteid, String token) throws IOException, ParseException {

		String userid = tokenutility.getUserToken(token);
		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.NOTE_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		}
		Notemodel note = id.get(); // get id in db

		note.setReminder(date); // set reminder
//		elasticsearchserviceImp.updateDocuemnt(note, noteid);

		repo.save(note);

		return new Response(200, MessageReference.REMINDER_SET__SUCCESSFULLY, true);
	}

	/**
	 * purpose create method for remove reminder
	 * 
	 * @throws IOException
	 */
	@Override
	public Response removeReminder(String noteid, String token) throws IOException {
		String userid = tokenutility.getUserToken(token);
		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.NOTE_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		}
		Notemodel note = id.get(); // get id in db

		note.setReminder(null); // remove reminder
//		elasticsearchserviceImp.updateDocuemnt(note, noteid);

		repo.save(note);

		return new Response(200, MessageReference.REMINDER_DELETE__SUCCESSFULLY, true);
	}

	@Override
	public Response setColor(String noteid, String colorcode, String token) throws IOException {

		String userid = tokenutility.getUserToken(token);
		Optional<Notemodel> id = repo.findByIdAndUserid(noteid, userid);
		if (id.isEmpty()) {
			throw new Usernotfoundexception(MessageReference.NOTE_ID_NOT_FOUND); // if userid not found throw exception
																					// user not found

		}
		Notemodel note = id.get(); // get id in db
		note.setColor(colorcode);
		elasticsearchserviceImp.updateDocuemnt(note, noteid);

		repo.save(note);

		return new Response(200, "color  set success", true);
	}

}
