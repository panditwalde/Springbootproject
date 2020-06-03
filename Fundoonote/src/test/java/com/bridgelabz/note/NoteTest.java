///******************************************************************************
// 1 *  Compilation:  javac -d bin NoteTest.java
// *  Execution:    
// *               
// *  
// *  Purpose:       create  NoteTest  for  write test case for user  note
// *
// *  @author  pandit walde
// *  @version 1.0
// *  @since   11-12-2019
// *
// *****************************************************************************/
//package com.bridgelabz.note;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.modelmapper.ModelMapper;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.bridgelabz.note.dto.Collabratordto;
//import com.bridgelabz.note.dto.Notedto;
//import com.bridgelabz.note.model.Notemodel;
//import com.bridgelabz.note.repo.Noterepository;
//import com.bridgelabz.note.response.Response;
//import com.bridgelabz.note.services.ElasticsearchserviceImp;
//import com.bridgelabz.note.services.NoteserviceImp;
//import com.bridgelabz.note.utility.Tokenutility;
//
//@SpringJUnitConfig
//@RunWith(SpringJUnit4ClassRunner.class)
//public class NoteTest {
//
//	@Mock
//	private Noterepository noterepo;   // create  mock object of  noterepository use for junit mokito
//	@InjectMocks
//	private NoteserviceImp noteserviceimp;  //create mock object of  note service imp for junit mokito
//	@Mock 
//	private ElasticsearchserviceImp elasticsearchserviceImp; // create  mock object of  elastic search for  junit mokito
//	@Mock
//	private ModelMapper mapper;  // create mock  object of  modelmapper  for junit mokito
// 	@Mock
// 	private Tokenutility tokenutility; // create mock object of tokenutilty for    create token and gettoken
//
//	Date date;
//	LocalDateTime datetime = LocalDateTime.now();
//	String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZGU4OWQwZDE3ZjBlMTcyNDJiMDQzMjciLCJpYXQiOjE1NzU1MjU2NDV9.LruI0fUkD23CDeZLL9TTOS4pffXwIqVP20UL1iFMP98";
//	String noteid = "5de62c4df3675a0154c6c634";
//	String userid = "5dddff500ed0504ee89964f0";
//	Notedto notedto = new Notedto();
//	Notemodel notemodel = new Notemodel();
//	Optional<Notemodel> idd = Optional.of(notemodel);
//	List<Notemodel> list = new ArrayList<Notemodel>();
//	Notemodel note1 = new Notemodel();
//	List<String> list1 = new ArrayList<String>();
//	Collabratordto collabratordto = new Collabratordto();
//
//	
//	/**
//	 *  purpose  -   create note for   test for note using junit mokito
//	 */
//	@Test
//	public void createNote() throws IOException {
//		when(mapper.map(notedto, Notemodel.class)).thenReturn(notemodel);     //return notemodel  for set new  info
//		when(tokenutility.getUserToken(token)).thenReturn(userid);            // return userid in token
//		notemodel.setUserid(userid);                                          // set the perticular  userid  perticluar note
//		when(noterepo.save(notemodel)).thenReturn(notemodel);                 // test  for note repo
//		when(elasticsearchserviceImp.createDocuemnt(notemodel)).thenReturn(null);  // store data in  elastic  search
//		Response res = noteserviceimp.createNote(notedto, token);             //return response of note service
//		assertEquals(200, res.getStatus());                                   // return response status
//
//	}
//
//	/**
//	 *   purpose  main purpose of  this for delete perticular note 
//	 * @throws IOException 
//	 */
//	@Test
//	public void deleteNote() throws IOException {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);         // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);  // return optinal idd find id and user 
//		noterepo.delete(idd.get());                                        //  call mehod  noterepo for delete pertucular note in  db
//		Response res = noteserviceimp.deletePermanentNote(noteid, token);  // return  response   of  noteservice
//		assertEquals(200, res.getStatus());  // return response status
//	}
//
//	/**
//	 * purpose -main purpose  of this  test for   search note in   db
//	 */
//	@Test
//	public void searchNote() {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);         // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);  // return optinal idd find id and user 
//		Response res = noteserviceimp.searchNote(noteid, token);           // return  response   of  noteservice
//		assertEquals(200, res.getStatus());                                // return response status
// 
//	}
//
//	/**
//	 *     purpose  main purpose  of  update user note 
//	 * @throws IOException 
//	 */
//	@Test
//	public void updateNote() throws IOException {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);          // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);   // return optinal idd find id and user 
//		note1.setTitle(notedto.getColor());                                 // set title  for updating
//		note1.setDescription(notedto.getDescription());                     // set description  for updating
//		note1.setColor(notedto.getColor());                                 // set  color  for updating
//		note1.setDate(datetime);                                            // set current date
//		when(noterepo.save(note1)).thenReturn(note1);                       //  store update data in  perticular note
//		Response res = noteserviceimp.UpdateNote(notedto, noteid, token);   //return response of note service
//		assertEquals(200, res.getStatus());                                 // return  response status
//
//	}
//
//	/**
//	 *   purpose - main purpose  for this method  add note in  archive
//	 * @throws IOException 
//	 */
//	@Test
//	public void archive() throws IOException {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);             // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);      // return optinal idd find id and user 
//		notemodel.setArchive(false);                                           // set  archive
//		when(noterepo.save(notemodel)).thenReturn(notemodel);                   // store  archive in db
//		Response res = noteserviceimp.trash(token, noteid);                     //return response of note service
//		assertEquals(200, res.getStatus());                                     // return  response status
//
//	}
//
//	/**
//	 *   purpose  create pin unpin
//	 * @throws IOException 
//	 */
//	@Test
//	public void pin() throws IOException {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);             // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);      // return optinal idd find id and user 
//		notemodel.setPin(false);                                                // set pin
//		when(noterepo.save(notemodel)).thenReturn(notemodel);                    // store pin in db
//		Response res = noteserviceimp.trash(token, noteid);                     //return response of note service
//		assertEquals(200, res.getStatus());                                     // return  response status
// 
//	}
//
//	/**
//	 *  purpose - store note in trash
//	 * @throws IOException 
//	 */
//	@Test
//	public void trash() throws IOException {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);              // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);       // return optinal idd find id and user 
//		notemodel.setTrash(false);                                              // set trash
//		when(noterepo.save(notemodel)).thenReturn(notemodel);                   // store trash in db
//		Response res = noteserviceimp.trash(token, noteid);                     //return response of note service
//		assertEquals(200, res.getStatus());                                      // return  response status
//
//	}
//
//	/**
//	 *   purpose - add reminder perticluar note
//	 * @throws IOException 
//	 */
//	@Test
//	public void addReminder() throws IOException {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);                // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);         // return optinal idd find id and user 
//		note1.setRemider(date);                                                    // set reminder
//		when(noterepo.save(note1)).thenReturn(note1);                               //store reminder in db
//		Response res = noteserviceimp.addReminder(date, noteid, token);            //return response of note service
//		assertEquals(200, res.getStatus());                                         // return  response status
//
//	}
//
//	/**
//	 *  purpose  remove reminder in   perticular note 
//	 * @throws IOException 
//	 */
//	@Test
//	public void removeReminder() throws IOException {                                                
//		when(tokenutility.getUserToken(token)).thenReturn(userid);                   // return userid in token
//		when(noterepo.findByIdAndUserid(noteid, userid)).thenReturn(idd);            // return optinal idd find id and user 
//		note1.setRemider(null);                                                         // set  remove remider
//		when(noterepo.save(note1)).thenReturn(note1);                                     // remove remider in db
//		Response res = noteserviceimp.removeReminder(noteid, token);                  //return response of note service
//		assertEquals(200, res.getStatus());                                          // return  response status
//
//	}
//
//	@Test
//	public void findLabelByUser_id() {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);
//
//	}
//
//	@Test
//	public void showAllNote() {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);
//		System.out.println(userid);
//		when(noterepo.findByUserid(userid)).thenReturn(list);
//		System.out.println(list);
//		when(list.stream().filter(i -> !i.isTrash() && !i.isArchive()).collect(Collectors.toList()));
//		System.out.println(list);
//		assertEquals(200, list);
//
//	}
//
//	@Test
//	public void sortByName() {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);
//		List<?> notelist = (List<?>) noteserviceimp.sortNoteByName(token);
//		assertEquals(notelist, noterepo.findAll());
//
//	}
//
//	@Test
//	public void sortByDate() {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);
//
//		List<?> notelist = (List<?>) noteserviceimp.sortNoteByDate(token);
//		assertEquals(notelist, noterepo.findAll());
//	}
//
//	@Test
//	public void addCollabrator() {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);
//
//		when(noterepo.findByIdAndUserid(collabratordto.getNoteId(), userid)).thenReturn(idd);
//		list1 = note1.getCollabrators();
//		list1.add(collabratordto.getColaboratorId());
//		note1.setCollabrators(list1);
//		when(noterepo.save(note1)).thenReturn(note1);
//		Response res = noteserviceimp.addCollabrator(collabratordto, token);
//		assertEquals(200, res.getStatus());
//
//	}
//
//}
