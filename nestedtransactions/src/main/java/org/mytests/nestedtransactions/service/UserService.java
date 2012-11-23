package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.User;

public interface UserService {
	
	public User createUser(User u);
	
	public User loadUser(Long long1);
	
	public void setFail(boolean fail);

}
