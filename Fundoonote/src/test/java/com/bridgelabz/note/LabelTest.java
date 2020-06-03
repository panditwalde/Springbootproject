///******************************************************************************
// 1 *  Compilation:  javac -d bin LabelTest.java
// *  Execution:    
// *               
// *  
// *  Purpose:       create  LabelTest  for  write test case for user  label
// *
// *  @author  pandit walde
// *  @version 1.0
// *  @since   11-12-2019
// *
// ******************************************************************************/
//
//package com.bridgelabz.note;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.modelmapper.ModelMapper;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.bridgelabz.note.dto.Labeldto;
//import com.bridgelabz.note.model.Labelmodel;
//import com.bridgelabz.note.repo.Labelrepository;
//import com.bridgelabz.note.response.Response;
//import com.bridgelabz.note.services.LabelserviceImp;
//import com.bridgelabz.note.utility.Tokenutility;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringJUnitConfig
//public class LabelTest {
//
//	@Mock
//	private Labelrepository labelrepo;   //create mock object of  labelrepository
//
//	@InjectMocks
//	private LabelserviceImp labelserviceImp; // create injectmocks  object of labelservice
//
//	@Mock
//	private ModelMapper mapper;      // create mock object of  modelmapper
//	@Mock
//	private Tokenutility tokenutility;  // create mock object of tokenutility
//
//	String token ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZGU4OWQwZDE3ZjBlMTcyNDJiMDQzMjciLCJpYXQiOjE1NzU1MjU2NDV9.LruI0fUkD23CDeZLL9TTOS4pffXwIqVP20UL1iFMP98";
//	String labelid ="5dd8dfd9086ed83f82560eee";
//	String userid = "5dddff500ed0504ee89964f0";
//	Labelmodel labelmodel = new Labelmodel();
//	Optional<Labelmodel> idd1 = Optional.of(labelmodel);
//	Labeldto labeldto = new Labeldto();
//	LocalDateTime datetime = LocalDateTime.now();
//
//	/**
//	 *   purpose - main purpose of  this method   create  label and test it
//	 */
//	@Test
//	public void createLabel() {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);             //return userid in token
//		when(mapper.map(labeldto, Labelmodel.class)).thenReturn(labelmodel);   //return notemodel  for set new  info
//		labelmodel.setUserid(userid);                                          //set  userid of user
//		when(labelrepo.save(labelmodel)).thenReturn(labelmodel);               //store in label in db
//		Response res = labelserviceImp.labelAdd(labeldto, token);              //return response of labelservice
//		assertEquals(200, res.getStatus());                                    //return response status
//
//	}
//
//	/**
//	 * purpose - main purpose of  this method   search  label in  list   and test it
//	 */
//	@Test
//	public void searchLabel() {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);            // return userid in token
//		when(labelrepo.findByIdAndUserid(labelid, userid)).thenReturn(idd1);  //return optinal idd find id and user 
//		when(labelrepo.findById(userid)).thenReturn(idd1);                    //search user present or not in db
//		Response res = labelserviceImp.labelSearch(labelid, token);           // return response of  label service
//		assertEquals(200, res.getStatus());                                   // return response status
//
//	}
//
//	/**
//	 *     purpose - main purpose of  this method  delete label  and test it
//	 */
//	@Test
//	public void DeleteLabel() {
//		when(tokenutility.getUserToken(token)).thenReturn(userid);            // return userid in token
//		when(labelrepo.findByIdAndUserid(labelid, userid)).thenReturn(idd1);  //return optinal idd find id and user 
//		labelrepo.deleteById(idd1);                                           // delete  label in db
//		Response res = labelserviceImp.labelDelete(labelid, token);           // return response of  delete label
//		assertEquals(200, res.getStatus());                                   // return response status
//		
//	}
//
//	/**
//	 *    purpose - main purpose of  this method  update label  and test it
//	 */
//	@Test
//	public void labelUpdate() {
//
//		when(tokenutility.getUserToken(token)).thenReturn(userid);            // return userid in token
//		when(labelrepo.findByIdAndUserid(labelid, userid)).thenReturn(idd1);   //return optinal idd find id and user 
//		labelmodel.setLable_title(labeldto.getLable_title());                  // set updated label
//		labelmodel.setUpdated_date(datetime);                                   // set  current date
//		when(labelrepo.save(labelmodel)).thenReturn(labelmodel);                //  store updated label in db
//		Response res=labelserviceImp.labelUpdate(labeldto, labelid, token);     // return response of updated label
//		assertEquals(200, res.getStatus());		                               //return response status
//		
//		
//	}
//
//	/**
//	 * purpose - main purpose of  this method  display  of all note  and test it
//	 */
//	@Test
//	public void showAllLabel() {
//		
//		ArrayList< Labelmodel> list=new ArrayList<Labelmodel>();   //create object of arraylist
//		when(tokenutility.getUserToken(token)).thenReturn(userid); // return userid in token
//		when(labelrepo.findByUserid(userid)).thenReturn(list);	   // check user  is present or not  in db
//		when(labelserviceImp.labelShowAll(token)).thenReturn(list); // if user label is found return it		
//		assertEquals(200, list);                                    // return  found label 
//		
//		
//	}
//	@Test
//	public void findLabelByUser_id() {
//		
//	}
//	
//	@Test
//	public void manyToMany() {
//		
//	}
//
//}
