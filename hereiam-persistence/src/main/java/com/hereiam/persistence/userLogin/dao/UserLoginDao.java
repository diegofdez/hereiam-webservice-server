package com.hereiam.persistence.userLogin.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import com.hereiam.persistence.exception.EntityAlreadyExistsException;
import com.hereiam.persistence.userLogin.model.UserLogin;

public class UserLoginDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public UserLogin createUserLogin(UserLogin userLogin) throws EntityAlreadyExistsException {
		UserLogin existentUserLogin = retrieveUserLoginForEmail(userLogin.getEmail());
		if (existentUserLogin != null) {
			throw new EntityAlreadyExistsException("Ther's already a user with email: " + userLogin.getEmail());
		}
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		UserLogin createdUserLogin = new UserLogin();
		createdUserLogin.setLogin(userLogin.getLogin());
		createdUserLogin.setPasswrd(userLogin.getPasswrd());
		
		try {
			createdUserLogin.setEmail((String) session.save(userLogin));
			tx.commit();
		}
		catch (Exception e) {
			tx.rollback();
		}
		finally {
			session.close();
		}
		
		return createdUserLogin;
	}
	
	public UserLogin retrieveUserLoginForEmail(String email) {
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(
                UserLogin.class);
        criteria.add(Restrictions.ilike("email", email + "%"));
        
        UserLogin result = (UserLogin) criteria.uniqueResult();
        session.close();
        return result;
	}
	
	public UserLogin updateUserLogin(UserLogin userLogin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		UserLogin updatedUserLogin = new UserLogin();
		
		try {
			session.update(userLogin);
			tx.commit();
			updatedUserLogin.setEmail(userLogin.getEmail());
			updatedUserLogin.setLogin(userLogin.getLogin());
			updatedUserLogin.setPasswrd(userLogin.getPasswrd());
		}
		catch (Exception e) {
			tx.rollback();
		}
		finally {
			session.close();
		}
		
		return userLogin;		
	}
}
