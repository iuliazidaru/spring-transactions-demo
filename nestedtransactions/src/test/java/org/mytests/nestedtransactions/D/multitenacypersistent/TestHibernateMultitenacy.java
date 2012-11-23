package org.mytests.nestedtransactions.D.multitenacypersistent;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mytests.nestedtransactions.model.HibUser;
import org.mytests.nestedtransactions.model.User;
import org.mytests.nestedtransactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
@TransactionConfiguration(transactionManager="txManager")
public class TestHibernateMultitenacy {
	@Autowired
	private UserService userService;
	@Autowired
	private SessionFactory sessionFactory;

	
	@BeforeTransaction
	public void before(){
		Session session = SessionFactoryUtils.openSession(sessionFactory);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}
	
	
	@AfterTransaction
	public void after(){
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		Session session = sessionHolder.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
	}
	
	@Test
	public void testSimpleTransaction(){
		//see log!!
		User u = userService.createUser(new HibUser());
		assertThat(userService.loadUser(u.getId()), is(u));
	}
	
}
