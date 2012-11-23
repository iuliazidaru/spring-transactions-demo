package org.mytests.nestedtransactions.dao;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.mytests.nestedtransactions.model.User;

public interface UserDao {
	
	public long createUser(User user);
	
	public Set<User> loadUsers();

	public User loadUser(Long id);
	
	public void setSessionFactory(SessionFactory sessionFactory);
}
