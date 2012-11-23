package org.mytests.nestedtransactions.C.simplepersistent;

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
public class TestWithHibernate {

	@Autowired
	private UserService userService;
	@Autowired
	private SessionFactory sessionFactory;
	
	@BeforeTransaction
	public void before(){
		//bind session to current thread
		Session session = SessionFactoryUtils.openSession(sessionFactory);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}
	
	@AfterTransaction
	public void after(){
		//unbind session from current thread
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		Session session = sessionHolder.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
	}
	
	@Test
	//@Rollback(value=false)
	public void testSimpleTransaction(){
		//see log!!
		/*
		 * The transaction is opened before the test by TransactionalTestExecutionListener
		 * and rolled back in order to keep the context clean.
		 * 
		 * With @Rollback(value=false) (see above), the transaction will commit.
		 */
		User u = userService.createUser(new HibUser());
		assertThat(userService.loadUser(u.getId()), is(u));
	}
}
