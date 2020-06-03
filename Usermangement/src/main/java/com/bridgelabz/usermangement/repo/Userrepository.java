package com.bridgelabz.usermangement.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.usermangement.model.Usermodel;

@Repository
public interface Userrepository extends MongoRepository<Usermodel, Object> {

	public Usermodel findByEmail(String email); // find user email

}
