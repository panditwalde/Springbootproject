package com.bridgelabz.usermangement.service;

import java.io.IOException;

import javax.mail.Multipart;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermangement.dto.Logindto;
import com.bridgelabz.usermangement.dto.Permissiondto;
import com.bridgelabz.usermangement.dto.Userdto;
import com.bridgelabz.usermangement.model.Usermodel;
import com.bridgelabz.usermangement.response.Response;

public interface Userservice {
	
	
	 public Response register(Userdto userdto,String token);
	 public  Response login(Logindto logindto);        //create loginUser() method for login user
     public Response forgotPassword(String email);
	 public Response updateUser(Usermodel user,String token);	 
	 public Response getUserList(String token);	 
	 public Response uploadProfilePicture(MultipartFile file,String token) throws IOException;
	 public Response logout(String token);
	 public Response loginHistory(String token);
	 public Response setPermission(Permissiondto permissiondto,String token);
	 public Response getPermission(String menuid,String token);	 
	 public Response menuItem(String menuname);


}
