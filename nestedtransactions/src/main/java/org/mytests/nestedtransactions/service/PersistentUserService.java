package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.dao.UserDao;
import org.mytests.nestedtransactions.model.User;

public class PersistentUserService implements UserService{
	private UserDao userDao;
	
	@Override
	public User createUser(User u) {
		System.out.println("PersistentUserService.createUser()");
		long id = userDao.createUser(u);	
		u.setId(id);
		return u;
	}

	

	@Override
	public void setFail(boolean fail) {
		
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}



	@Override
	public User loadUser(Long id) {
		System.out.println("PersistentUserService.loadUser()");
		return userDao.loadUser(id);
	}

}
