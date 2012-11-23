package org.mytests.nestedtransactions.D.multitenacypersistent;

import org.mytests.nestedtransactions.model.User;
import org.mytests.nestedtransactions.service.UserService;

/**
 * class used for testing purpose - monitor userService behavoiur.
 * @author Iulia
 *
 */
public class UserServiceProxy implements UserService{
	private UserService userService;
	private User lastUserCreated;
	
	public UserServiceProxy(UserService userService) {
		super();
		this.userService = userService;
	}

	public User createUser(User u) {
		User user = userService.createUser(u);
		lastUserCreated = user;					
		return lastUserCreated;
	}

	public User loadUser(Long long1) {
		return userService.loadUser(long1);
	}

	public void setFail(boolean fail) {
		userService.setFail(fail);
	}

	public User getLastUserCreated() {
		return lastUserCreated;
	}
	
	
}
