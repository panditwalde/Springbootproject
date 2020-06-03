package com.bridgelabz.usermangement.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermangement.model.Onlineusermodel;

public interface Onlineuserrepository  extends MongoRepository<Onlineusermodel, Object>{
	
	

}
