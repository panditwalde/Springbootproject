package com.bridgelabz.usermangement.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermangement.dto.Logindto;
import com.bridgelabz.usermangement.dto.Permissiondto;
import com.bridgelabz.usermangement.dto.Userdto;
import com.bridgelabz.usermangement.model.Usermodel;
import com.bridgelabz.usermangement.response.Response;
import com.bridgelabz.usermangement.service.UserserviceImp;

import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/")
@PropertySource("classpath:message.properties")

public class Usercontroller {

	@Autowired
	UserserviceImp userserviceimp;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody Userdto userdto, @RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.register(userdto, token), HttpStatus.OK);
	}

	@PutMapping("/user")
	public ResponseEntity<Response> updateUser(@RequestBody Usermodel user, @RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.updateUser(user, token), HttpStatus.OK);

	}

	@GetMapping("/geruser")
	public ResponseEntity<Response> getUser(@RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.getUserList(token), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody Logindto logindto) {
		return new ResponseEntity<Response>(userserviceimp.login(logindto), HttpStatus.OK);
	}

	@GetMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam String email) {

		return new ResponseEntity<Response>(userserviceimp.forgotPassword(email), HttpStatus.OK);
	}

	@PutMapping(value = "/addprofile", consumes = "multipart/form-data")
	public ResponseEntity<Response> uploadProfilePicture(MultipartFile file, @RequestHeader String token)
			throws IOException {

		return new ResponseEntity<Response>(userserviceimp.uploadProfilePicture(file, token), HttpStatus.OK);
	}

	@GetMapping("/logout")
	public ResponseEntity<Response> logout(@RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.logout(token), HttpStatus.OK);
	}

	@GetMapping("/loginhistory")
	public ResponseEntity<Response> loginHistory(@RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.loginHistory(token), HttpStatus.OK);
	}

	@PostMapping("/setpermission")
	public ResponseEntity<Response> setPermission(@RequestBody Permissiondto permissiondto,
			@RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.setPermission(permissiondto, token), HttpStatus.OK);
	}

	@GetMapping("/getpermission")
	public ResponseEntity<Response> getPermission(@RequestParam String menuid, @RequestHeader String token) {

		return new ResponseEntity<Response>(userserviceimp.getPermission(menuid, token), HttpStatus.OK);

	}

	@PostMapping("/menuitem")
	public ResponseEntity<Response> addMenuItem(@RequestParam String menuname) {

		return new ResponseEntity<Response>(userserviceimp.menuItem(menuname), HttpStatus.OK);
	}

}
