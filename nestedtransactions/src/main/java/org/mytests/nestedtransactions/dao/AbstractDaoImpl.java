package org.mytests.nestedtransactions.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 * Parent class for all DAO classes.
 * @author Iulia
 *
 */
public class AbstractDaoImpl {

	private SessionFactory sessionFactory;

	public AbstractDaoImpl() {
		super();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	
}