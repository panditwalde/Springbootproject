package com.bridgelabz.usermangement.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermangement.model.Mastermenumodel;

public interface Mastermenurepository extends MongoRepository<Mastermenumodel, Object> {
	     

}
