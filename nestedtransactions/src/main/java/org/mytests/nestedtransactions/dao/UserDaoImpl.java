package org.mytests.nestedtransactions.dao;

import java.util.Set;

import org.mytests.nestedtransactions.model.HibUser;
import org.mytests.nestedtransactions.model.User;

public class UserDaoImpl extends AbstractDaoImpl implements UserDao {
	@Override
	public long createUser(User user) {
		return Long.parseLong(getSession().save(user).toString());		
	}

	@Override
	public Set<User> loadUsers() {
		return null;
	}

	@Override
	public User loadUser(Long id) {
		return (User)getSession().byId(HibUser.class).load(id);		
	}

}
