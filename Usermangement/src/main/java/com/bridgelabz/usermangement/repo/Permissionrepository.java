package com.bridgelabz.usermangement.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermangement.model.Permissionmodel;

public interface Permissionrepository  extends MongoRepository<Permissionmodel, Object>{	
	
	Permissionmodel findByUserid(String userid);
	
	List<Permissionmodel> findByUseridAndMenuid(String userid,String menuid);

}
