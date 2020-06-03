
package com.bridgelabz.usermangement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Document(collection = "login_history")

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginhistoryModel {

	@Id
	private String id;
	private String userid;
	private List<LocalDateTime> Loginhistory = new ArrayList<LocalDateTime>();
	private List<LocalDateTime> Logouthistory = new ArrayList<LocalDateTime>();


}
