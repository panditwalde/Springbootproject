package com.bridgelabz.note.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.note.model.Collabratormodel;
import com.bridgelabz.note.model.Labelmodel;

public interface Collabratorrepository extends MongoRepository<Collabratormodel, Object> {
	
	
	public Optional<Collabratormodel> findByColaboratorUsernameAndOwnerId(String labelid, String userid);

}
