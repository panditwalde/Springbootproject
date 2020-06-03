package com.bridgelabz.usermangement.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.bridgelabz.usermangement.dto.Logindto;
import com.bridgelabz.usermangement.dto.Permissiondto;
import com.bridgelabz.usermangement.dto.Userdto;
import com.bridgelabz.usermangement.exception.CustomException;
import com.bridgelabz.usermangement.model.LoginhistoryModel;
import com.bridgelabz.usermangement.model.Mastermenumodel;
import com.bridgelabz.usermangement.model.Onlineusermodel;
import com.bridgelabz.usermangement.model.Permissionmodel;
import com.bridgelabz.usermangement.model.Rabbitmqmodel;
import com.bridgelabz.usermangement.model.Usermodel;
import com.bridgelabz.usermangement.repo.Loginhistoryrepository;
import com.bridgelabz.usermangement.repo.Mastermenurepository;
import com.bridgelabz.usermangement.repo.Onlineuserrepository;
import com.bridgelabz.usermangement.repo.Permissionrepository;
import com.bridgelabz.usermangement.repo.Userrepository;
import com.bridgelabz.usermangement.response.Response;
import com.bridgelabz.usermangement.utlity.Tokenutility;
import com.bridgelabz.usermangement.utlity.Utility;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class UserserviceImp implements Userservice {

	@Autowired
	private Userrepository userrepo;
	@Autowired
	private Loginhistoryrepository loginhistoryrepo;

	@Autowired
	private Mastermenurepository mastermenurepository;

	@Autowired
	private Onlineuserrepository onlineuserrepository;

	@Autowired
	private Permissionrepository permissionrepository;

	@Autowired
	private Environment enviroment;
	@Autowired
	private ModelMapper mapper; // user modelmapper for store data

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private Tokenutility tokenutility; // create object for tokenutility

	@Override
	public Response register(@Valid Userdto userdto, String token) {

		String userid = tokenutility.getUserToken(token);
		userrepo.findById(userid)
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND")));

		Usermodel user = mapper.map(userdto, Usermodel.class);
		if (userrepo.findAll().stream().anyMatch(i -> i.getEmail().equals(userdto.getEmail()))) {
			throw new CustomException.RegistrationException(enviroment.getProperty("EMAIL_ALREADY_REGISTERED"));
		}

		user.setPassword(encryptPassword(userdto.getPassword()));
		LocalDateTime datetime = LocalDateTime.now();
		user.setCreatedDate(datetime);

		userrepo.save(user);

		return new Response(200, enviroment.getProperty("USER_ADD_SUCCESSFULLY"), true);
	}

	@Override
	public Response login(Logindto logindto) {

		Usermodel user = userrepo.findAll().stream().filter(i -> i.getEmail().equals(logindto.getEmail())).findAny()
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND")));

		if (user.getUserRole().equals("admin")) {
			throw new CustomException.usernotallowpermission(enviroment.getProperty("PERMISSIONDENIED"));
		}

		Onlineusermodel onlineusermodel;
		List<Onlineusermodel> onlinelist = onlineuserrepository.findAll();
		if (onlinelist.size() == 0) {
			onlineusermodel = new Onlineusermodel();
			List<String> userids = new ArrayList<String>();
			if (userids.contains(user.getId())) {
				return new Response(400, enviroment.getProperty("USERALREADYLOGGING"), null);
			}
			userids.add(user.getId());
			onlineusermodel.setUserid(user.getId());
			onlineusermodel.setUsercount(1);
			onlineuserrepository.save(onlineusermodel);
		} else {
			onlineusermodel = onlinelist.get(0);
			List<String> userids = new ArrayList<String>();
			if (userids.contains(user.getId())) {
				return new Response(400, enviroment.getProperty("USERALREADYLOGGING"), null);
			}
			userids.add(user.getId());
			onlineusermodel.setUserid(user.getId());
			onlineusermodel.setUsercount(onlineusermodel.getUsercount() + 1);
			onlineuserrepository.save(onlineusermodel);

		}

		String token = tokenutility.createToken(user.getId());
		String password = decryptPassword(user.getPassword());
		System.out.println(password.matches(logindto.getPassword()));
		if (user.getEmail().equals(logindto.getEmail()) && password.equals(logindto.getPassword())) {

			Optional<LoginhistoryModel> logindata = loginhistoryrepo.findByUserid(user.getId());
			List<LocalDateTime> list = new ArrayList<LocalDateTime>();
			LoginhistoryModel loginhistoryModel;
			if (logindata.isEmpty()) {

				loginhistoryModel = new LoginhistoryModel();
				loginhistoryModel.setUserid(user.getId());
				list.add(LocalDateTime.now());
				loginhistoryModel.setLoginhistory(list);
				loginhistoryrepo.save(loginhistoryModel);

			} else {
				loginhistoryModel = logindata.get();
				list = loginhistoryModel.getLoginhistory();
				list.add(LocalDateTime.now());
				loginhistoryModel.setLoginhistory(list);
				loginhistoryrepo.save(loginhistoryModel);
			}

			return new Response(200, enviroment.getProperty("LOGIN_SUCCESSFULLY"), token);
		}
		return new Response(200, enviroment.getProperty("LOGIN_NOT_SUCCESSFULLY"), true);
	}

	@Override
	public Response forgotPassword(String email) {

		Usermodel user = userrepo.findByEmail(email);

		Rabbitmqmodel body = Utility.getRabbitMq(user.getEmail(), decryptPassword(user.getPassword()));
		template.convertAndSend("userMessageQueue", body);

		return new Response(200, "your password", decryptPassword(user.getPassword()));
	}

	/**
	 * @Purpose - Used to encrypt password
	 * @param accepts password in the form of string
	 * @return password in encrypted format as a string
	 */
	public String encryptPassword(String password) {
		return Base64.getEncoder().encodeToString(password.getBytes());
	}

	/**
	 * @Purpose - Used to decrypt Password
	 * @param accepts decrypted password in the form of string
	 * @return password in decrypted format as a string
	 */
	public String decryptPassword(String encryptedPassword) {
		return new String(Base64.getMimeDecoder().decode(encryptedPassword));
	}

	@Override
	public Response updateUser(Usermodel updateuser, String token) {
		String userid = tokenutility.getUserToken(token);
		Usermodel user = userrepo.findById(userid)
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND")));

		System.out.println(user);
		System.out.println(updateuser);
		user = updateuser;

		LocalDateTime datetime = LocalDateTime.now();
		user.setUpdatedDate(datetime);

		System.out.println(user);
		userrepo.save(user);
		return new Response(200, enviroment.getProperty("USERUPDAESUCCESSFULL"), true);
	}

	@Override
	public Response getUserList(String token) {
		String userid = tokenutility.getUserToken(token);

		return new Response(200, enviroment.getProperty("GETUSERLIST"), userrepo.findById(userid)
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND"))));
	}

	@Override
	public Response uploadProfilePicture(MultipartFile file, String token) throws IOException {
		String userid = tokenutility.getUserToken(token); // verfiy userid present or not
		Optional<Usermodel> getUser = userrepo.findById(userid); // if user not found genrated throws exception user not
																	// found
		if (getUser.isEmpty()) {
			throw new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND"));
		}
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dr3elpkt6", "api_key",
				"734447837289933", "api_secret", "8e62pWlW-1i52pOVViWu6hOMAg0"));
		// create object of cloudinary wehre objectutils map cloud inary object
		Usermodel user = getUser.get();
		if (user != null && userid != null) {
			if (file.getOriginalFilename().contains(".jpg") || file.getOriginalFilename().contains(".png")
					|| file.getOriginalFilename().contains(".jpeg")) { // only jpeg,jpg,png image is allowed
				if (!file.isEmpty()) {
					File filepath = new File(
							"/home/user/Documents/Springboot/Fundoouser/Profile/" + file.getOriginalFilename()); // give
																													// path
																													// where
																													// want
																													// to
																													// store
																													// image
					String fileName = StringUtils.cleanPath(file.getOriginalFilename());
					Path getPath = Paths.get("/home/user/Documents/Springboot/Fundoouser/Profile/");
					Path targetLocation = getPath.resolve(fileName);
					File toUpload = new File(targetLocation.toString());
					Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("toupload" + toUpload);
					filepath.createNewFile();
					FileOutputStream fo = new FileOutputStream(file.getOriginalFilename());
					Map<?, ?> uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
					user.setProfilePicture(uploadResult.get("secure_url").toString());
					fo.write(file.getBytes());
					userrepo.save(user);
					return new Response(200, enviroment.getProperty("PROFILEUPLOAD"),
							uploadResult.get("secure_url").toString());
				}
			}
		}
		return new Response(200, enviroment.getProperty("USERNOTFOUND"), true);

	}

	@Override
	public Response logout(String token) {

		String userid = tokenutility.getUserToken(token);
		Usermodel user = userrepo.findById(userid)
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND")));

		
		Onlineusermodel onlineUsers;
		List<Onlineusermodel> onlineUsersList = onlineuserrepository.findAll();
		onlineUsers = onlineUsersList.get(0);
		String userIds1 = onlineUsers.getUserid();
		if (!userIds1.equals(user.getId())) {
		return new Response(400, enviroment.getProperty("userAlreadyLogedout"),  null);
		}
		
		 ArrayList<String> userIds=new ArrayList<String>();
		 userIds.add(userIds1);
		userIds.remove(user.getId());
		onlineUsers.setUserid(user.getId());
		onlineUsers.setUsercount(onlineUsers.getUsercount() - 1);
		onlineuserrepository.save(onlineUsers);

		Optional<LoginhistoryModel> logoutHistoryData = loginhistoryrepo.findByUserid(user.getId());
		LoginhistoryModel logoutHistory = logoutHistoryData.get();
		List<LocalDateTime> logoutHistoryList = logoutHistory.getLogouthistory();
		logoutHistoryList.add(LocalDateTime.now());
		logoutHistory.setLoginhistory(logoutHistoryList);
		loginhistoryrepo.save(logoutHistory);
		
		
		return new Response(200, enviroment.getProperty("LOGOUTSUCCESSFULL") , true);
	}

	@Override
	public Response loginHistory(String token) {
		String userid = tokenutility.getUserToken(token);
		LoginhistoryModel loginhistoryModel = loginhistoryrepo.findByUserid(userid)
				.orElseThrow(() -> new CustomException.UserNotFoundException(enviroment.getProperty("USERNOTFOUND")));
		;
		return new Response(200, "", loginhistoryModel.getLoginhistory());
	}

	@Override
	public Response setPermission(Permissiondto permissiondto, String token) {

		String userid = tokenutility.getUserToken(token);
		Permissionmodel permissionmodel = mapper.map(permissiondto, Permissionmodel.class);
		permissionmodel.setUserid(userid);
		permissionrepository.save(permissionmodel);
		return new Response(200, enviroment.getProperty("PERMISSIONSET"), true);
	}

	@Override
	public Response getPermission(String menuid, String token) {
		String userid = tokenutility.getUserToken(token);
		ArrayList<Permissionmodel> list = (ArrayList<Permissionmodel>) permissionrepository
				.findByUseridAndMenuid(userid, menuid);

		return new Response(200, enviroment.getProperty("USERPERMISSION"), list);
	}

	@Override
	public Response menuItem(String menuname) {

		Mastermenumodel mastermenumodel = new Mastermenumodel();
		mastermenumodel.setMenuName(menuname);
		mastermenumodel.setCreated_date(LocalDateTime.now());
		mastermenurepository.save(mastermenumodel);

		return new Response(200, enviroment.getProperty("MENUITEMADD"), true);
	}

}
