package com.epam.publicenemies.service.impl;

import org.apache.log4j.Logger;

import com.epam.publicenemies.dao.IUserDao;
import com.epam.publicenemies.domain.User;
import com.epam.publicenemies.dto.UserDto;
import com.epam.publicenemies.service.IUserManagerService;
import com.epam.publicenemies.web.RegisterUserFormController;

public class UserManagerServiceImpl implements IUserManagerService {
	
	private Logger	log	= Logger.getLogger(RegisterUserFormController.class);
	
	private IUserDao userDao;

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	private UserDto makeUserDto(User user) {		
		return new UserDto(user);
	}

	/**
	 * Registers user based on email, pass and nickname
	 * First of all it inserts new entry into db and returns id of inserted entry
	 * After that - fetches inserted entry from bd by id and builds User object and UserDto object
	 * It seems to be unnecessary to write and then read from bd, 
	 * but in this way we'll be sure that transaction done well and get more fields
	 */
	@Override
	public UserDto registerNewUser(String uEmail, String uPasswd, String uNickName) {
		/* to understand action below:
		int insertedUserId = userDao.registerUser(uEmail, uPasswd, uNickName);
		// get inserted user
		User insertedUser = userDao.findUserById(insertedUserId);
		UserDto returnObj = new UserDto(insertedUser);
		return returnObj; */		
		return new UserDto(userDao.findUserById(userDao.registerUser(uEmail,
				uEmail, uNickName)));
		/* Old code here
		User user = new User();
		user.setUserId(userDTO.getUserId());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		userDao.registerUser(user);
		return new UserDto(); */
	}

	@SuppressWarnings("deprecation")
	@Override
	public UserDto getUserByEmailAndPassword(String name, String password) {
		User user = userDao.findUserByEmailAndPassword(name, password);
		if (user == null) {
			log.info("User wasn't found with email - " + name + " and pass - "
					+ password);
			return null;
		}
		return makeUserDto(user);
	}

	@Override
	public UserDto findUserByEmail(String email) {
		User user = userDao.findUserByEmail(email);
		if (user == null) {
			log.info("User wasn't found with email - " + email);
			return null;
		}
		return makeUserDto(user);
	}
}
