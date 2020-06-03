package com.bridgelabz.usermangement.exception;


public class CustomException {

	public static class UserNotFoundException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String message) {
 
			super(message);
		}

	}
	
	
	public static class InvalidTokenException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidTokenException(String message) {
 
			super(message);
		}

	}
	
	public static class RegistrationException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RegistrationException(String message) {
 
			super(message);
		}

	}
	
	public static class usernotallowpermission extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public usernotallowpermission(String message) {
 
			super(message);
		}

	}
	

}
