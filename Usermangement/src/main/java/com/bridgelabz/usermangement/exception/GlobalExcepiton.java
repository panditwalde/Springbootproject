package com.bridgelabz.usermangement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.usermangement.response.Response;

public class GlobalExcepiton extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ExceptionHandler(CustomException.UserNotFoundException.class)
	public ResponseEntity<Response> userNotFoundException(CustomException.UserNotFoundException e) {

		return new ResponseEntity<Response>(new Response(400, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomException.InvalidTokenException.class)
	public ResponseEntity<Response> invalidTokenException(CustomException.InvalidTokenException e) {

		return new ResponseEntity<Response>(new Response(400, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomException.RegistrationException.class)
	public ResponseEntity<Response> registrationException(CustomException.RegistrationException e) {

		return new ResponseEntity<Response>(new Response(400, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CustomException.usernotallowpermission.class)
	public ResponseEntity<Response> userNotAllowPermission(CustomException.usernotallowpermission e) {

		return new ResponseEntity<Response>(new Response(400, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}

}
