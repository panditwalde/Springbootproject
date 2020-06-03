package com.bridgelabz.note.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "collabrator")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collabratormodel {

	@Id
	String id;
	String ownerId;
	String colaboratorUsername;
	@DBRef(lazy = true)
	List<Notemodel> collabratorlist = new ArrayList<Notemodel>();

}
