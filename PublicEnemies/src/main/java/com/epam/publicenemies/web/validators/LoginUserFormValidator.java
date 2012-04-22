package com.epam.publicenemies.web.validators;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.epam.publicenemies.domain.User;
import com.epam.publicenemies.dto.UserDto;
import com.epam.publicenemies.service.IUserManagerService;

public class LoginUserFormValidator implements Validator
{
	private Logger				log	= Logger.getLogger(RegisterUserFormValidator.class);
	private IUserManagerService	userManagerService;
	public void setUserManagerService(IUserManagerService userManagerService)
	{
		this.userManagerService = userManagerService;
	}
	public boolean supports(Class<?> clazz)
	{
		return clazz.equals(User.class);
	}
	public void validate(Object obj, Errors errors)
	{
		log.info("VALIDATING LOGIN FORM....");
		User user = (User) obj;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty");
		if (errors.hasErrors()) return;
		UserDto uDTO = userManagerService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (uDTO == null)
		{
			log.error("USER WITH EMAIL: " + user.getEmail() + " AND PASSWORD: " + user.getPassword() + " NOT FOUND");
			errors.rejectValue("password", "user.not-found");
		}
	}
}
