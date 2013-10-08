package com.hereiam.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hereiam.persistence.exception.EntityAlreadyExistsException;
import com.hereiam.persistence.userLogin.dao.UserLoginDao;
import com.hereiam.persistence.userLogin.model.UserLogin;

@Controller
public class LoginController {
	
	Logger LOG = Logger.getLogger(LoginController.class);
	
	@Autowired
	UserLoginDao userLoginDao;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView createUser(@RequestBody(required = false) UserLogin userLoginRequest) {

		LOG.info("REceived body: " + userLoginRequest);
		
		String result = null;
		try {
			UserLogin response = userLoginDao.createUserLogin(userLoginRequest);
			response.setPasswrd("*****");
			result = "" + response;
		}
		catch (EntityAlreadyExistsException e) {
			result = e.getMessage();
		}
		
		
		final ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("response", result);
		return mav;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView checkUser(@RequestBody(required = false) UserLogin userLoginRequest) {

		LOG.info("Received body: " + userLoginRequest);
		
		String resultMessage = "OK";
		Integer resultCode = 200;
		UserLogin userLoginInDb = userLoginDao.retrieveUserLoginForEmail(userLoginRequest.getEmail());
		if (userLoginInDb == null) {
			resultCode = 400;
			resultMessage = "User doesn't exist";
		}
		else if (! userLoginRequest.getPasswrd().equals(userLoginInDb.getPasswrd())) {
			resultCode = 400;
			resultMessage = "Wrong password";
		}
		
		
		final ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("result", resultCode);
		mav.addObject("message", resultMessage);
		return mav;
	}

	public UserLoginDao getUserLoginDao() {
		return userLoginDao;
	}

	public void setUserLoginDao(UserLoginDao userLoginDao) {
		this.userLoginDao = userLoginDao;
	}
}
