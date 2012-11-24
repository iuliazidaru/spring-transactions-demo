package org.mytests.nestedtransactions.E.multipledatasources;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mytests.nestedtransactions.D.multitenacypersistent.UserServiceProxy;
import org.mytests.nestedtransactions.model.User;
import org.mytests.nestedtransactions.service.ContentService;
import org.mytests.nestedtransactions.service.ProvisioningService;
import org.mytests.nestedtransactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
/*@Transactional
@TransactionConfiguration(transactionManager="JtaTransactionManager")*/
public class TestTransactionsOverMultiTenantAndSimpleJdbcDataSource {
	
	@Autowired
	private ProvisioningService provisioningService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ContentService contentService;
	
	@Autowired
	private SessionFactory sessionFactoryUsers;
	
	@Autowired
	private SessionFactory sessionFactoryCollections;
	
	@Autowired
	private JtaTransactionManager transactionManager;
	
	@Before
	public void before(){
		//must bind the session to current thread for making Hibernate work
		//<prop key="current_session_context_class">org.springframework.orm.hibernate4.SpringSessionContext</prop>
		// SpringSessionContext looks into TransactionSynchronizationManager
		Session sessionUsers = SessionFactoryUtils.openSession(sessionFactoryUsers);
		TransactionSynchronizationManager.bindResource(sessionFactoryUsers, new SessionHolder(sessionUsers));
		
		Session sessionCollection = SessionFactoryUtils.openSession(sessionFactoryCollections);
		TransactionSynchronizationManager.bindResource(sessionFactoryCollections, new SessionHolder(sessionCollection));
	}
	
	@After
	public void after(){
		SessionHolder sessionHolderUsers = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactoryUsers);
		Session sessionUsers = sessionHolderUsers.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactoryUsers);
		if(sessionUsers.isOpen()){
			SessionFactoryUtils.closeSession(sessionUsers);
		}
		
		SessionHolder sessionHolderCollections = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactoryCollections);
		Session sessionCollections = sessionHolderCollections.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactoryCollections);
		if(sessionCollections.isOpen()){
			SessionFactoryUtils.closeSession(sessionCollections);
		}
	}
	
	
	@Test
	public void addInTwoDataSourcesShouldInsertInBoth(){		
		contentService.setFail(false);
		//do in transaction!
		DefaultTransactionDefinition transactionDef = new DefaultTransactionDefinition();
		transactionDef.setName("my transaction");
		TransactionStatus transactionStatus = null;
		transactionStatus = transactionManager.getTransaction(transactionDef);
					
		User u = provisioningService.allocateAccount();
		transactionManager.commit(transactionStatus);
		
		//check expected!
		assertThat(userService.loadUser(u.getId()), is(u));
		
		assertThat(contentService.loadCollection(u.getCollection().getId()), is(u.getCollection()));
	}
	
	
	@Test
	public void addInTwoDataSourcesShouldRollbackBothOnFailre(){		
		contentService.setFail(true);
		//manually create a proxy over userService class
		//in order to obtain the last user created 
		UserServiceProxy userServiceInspectProxy = new UserServiceProxy(userService);		
		provisioningService.setUserService(userServiceInspectProxy);
		
		//do in transaction!
		DefaultTransactionDefinition transactionDef = new DefaultTransactionDefinition();
		transactionDef.setName("my transaction");
		TransactionStatus transactionStatus = null;
		try{			
			transactionStatus = transactionManager.getTransaction(transactionDef);
			
			provisioningService.allocateAccount();
			fail("Allocalte account should have thrown a RuntimeException!");
		} catch (RuntimeException e){
			//expected
			transactionManager.rollback(transactionStatus);
		}

		User u = userServiceInspectProxy.getLastUserCreated();//last user created
		
		assertThat(userService.loadUser(u.getId()), nullValue());

	}
}
