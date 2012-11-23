package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.User;

public class DefaultUserService implements UserService{
	private boolean fail = false;
	
	
	public User createUser(User u) {
		System.out.println("DefaultUserService.createUser()");
		if(fail){
			throw new RuntimeException();
		}
		return u;
	}

	

	@Override
	public void setFail(boolean fail) {
		this.fail = fail;
	}



	@Override
	public User loadUser(Long long1) {
		return null;
	}
	
	
	

}
