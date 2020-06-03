/******************************************************************************
 *  Compilation:  javac -d bin Tokenutility.java
 *  Execution:    
 *               
 *  
 *  Purpose:       create utility for jwt  response in  token
 *
 *  @author  pandit walde
 *  @version 1.0
 *  @since  19-11-2019
 *
 ******************************************************************************/
package com.bridgelabz.usermangement.utlity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.bridgelabz.usermangement.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// create utility for perform jwt task
@Component
public class Tokenutility {

	@Autowired
	private Environment enviroment;

	private final String SECRET_KEY = "secret"; //

	/**
	 * @param id provide user id for generate token
	 * @return generate token and send the user mail id
	 */
	public String createToken(String id) {

		return Jwts.builder().setSubject(id).setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	/**
	 * @param token generate token user send response for user
	 * @return return token subject
	 */
	public String getUserToken(String token) {
		Claims claim = null;
		try {
			System.out.println("token :-" + token);
			claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

			System.out.println(claim.getSubject());

		} catch (Exception e) {
			throw new CustomException.InvalidTokenException(enviroment.getProperty("INVALID_TOKEN"));
		}
		return claim.getSubject();

	}

}
