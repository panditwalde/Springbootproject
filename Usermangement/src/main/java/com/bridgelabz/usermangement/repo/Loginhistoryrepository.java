package com.bridgelabz.usermangement.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermangement.model.LoginhistoryModel;

public interface Loginhistoryrepository extends MongoRepository<LoginhistoryModel, Object>{
	
	Optional<LoginhistoryModel> findByUserid(String userid);
	
	
	

}
