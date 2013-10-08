package com.hereiam.persistence.userLogin.dao;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hereiam.persistence.exception.EntityAlreadyExistsException;
import com.hereiam.persistence.userLogin.model.UserLogin;

public class UserLoginDaoTest {

	@Test
	public void testCreateUserLogin() throws EntityAlreadyExistsException {
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(new String[]{"classpath:spring-dao.xml"});
		
		UserLoginDao userLoginDao = (UserLoginDao) appContext.getBean("userLoginDao");
		
		UserLogin userLogin = new UserLogin();
		userLogin.setEmail(new Long(Calendar.getInstance().getTimeInMillis()).toString());
		userLogin.setPasswrd("bbbb");
		userLogin.setLogin("cccc");
		UserLogin createdUserLogin = userLoginDao.createUserLogin(userLogin);
		appContext.close();
		
		Assert.assertEquals(userLogin, createdUserLogin);
	}
	
	@Test
	public void testUpdateUserLogin() throws EntityAlreadyExistsException {
		ClassPathXmlApplicationContext appContext = 
				new ClassPathXmlApplicationContext(new String[]{"classpath:spring-dao.xml"});
		
		UserLoginDao userLoginDao = (UserLoginDao) appContext.getBean("userLoginDao");
		
		UserLogin userLogin = new UserLogin();
		userLogin.setEmail(new Long(Calendar.getInstance().getTimeInMillis()).toString());
		userLogin.setPasswrd("bbbb");
		userLogin.setLogin("cccc");
		userLoginDao.createUserLogin(userLogin);
		
		userLogin.setPasswrd("dddd");
		userLogin.setLogin("eeee");
		UserLogin updatedUserLogin = userLoginDao.updateUserLogin(userLogin);
		
		appContext.close();
		
		Assert.assertEquals(userLogin, updatedUserLogin);
	}
	
	

}
