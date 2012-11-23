package org.mytests.nestedtransactions.D.multitenacypersistent;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mytests.nestedtransactions.dao.UserDao;
import org.mytests.nestedtransactions.model.HibUser;
import org.mytests.nestedtransactions.model.User;
import org.mytests.nestedtransactions.service.ContentService;
import org.mytests.nestedtransactions.service.ProvisioningService;
import org.mytests.nestedtransactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestHibernateMultitenacyProgrammaticTM {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProvisioningService provisioningService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private UserDao userDAO;

	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	@Qualifier("sessionFactoryUS1")
	private SessionFactory sessionFactoryUS1;
	
	@Autowired
	@Qualifier("sessionFactoryEU1")
	private SessionFactory sessionFactoryEU1;
	
	
	@Autowired
	private DataSource dataSourceUS1;
	


	
	@Before
	public void before(){
		//must bind the session to current thread for making Hibernate work
		Session session = SessionFactoryUtils.openSession(sessionFactory);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		
		Session sessionUS = SessionFactoryUtils.openSession(sessionFactoryUS1);
		TransactionSynchronizationManager.bindResource(sessionFactoryUS1, new SessionHolder(sessionUS));
		
		Session sessionEU = SessionFactoryUtils.openSession(sessionFactoryEU1);
		TransactionSynchronizationManager.bindResource(sessionFactoryEU1, new SessionHolder(sessionEU));
		
		System.out.println("TestHibernateMultitenacyProgrammaticTM.before()" + sessionFactory.getAllClassMetadata());
	}
	
	
	@After
	public void after(){
		//must bind the session to current thread for making Hibernate work
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		Session session = sessionHolder.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
		
		
		SessionHolder sessionHolderUS = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactoryUS1);
		Session sessionUS = sessionHolderUS.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactoryUS1);
		SessionFactoryUtils.closeSession(sessionUS);
		
		SessionHolder sessionHolderEU = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactoryEU1);
		Session sessionEU = sessionHolderEU.getSession();
		TransactionSynchronizationManager.unbindResource(sessionFactoryEU1);
		SessionFactoryUtils.closeSession(sessionEU);
	}


	


	@Test//without TM it works?!
	public void testExpectedDataSourceUsed(){
		useDefaultMultitenantSessionFactory();
		User u = userService.createUser(new HibUser());
		assertThat(userService.loadUser(u.getId()), is(u));
		
		useSimpleDatasourceSessionFactory("US1");
		assertThat(userService.loadUser(u.getId()), is(u));
		
		useSimpleDatasourceSessionFactory("EU1");
		assertThat(userService.loadUser(u.getId()), nullValue());
	}


	private void useSimpleDatasourceSessionFactory(String datasourceId) {
		if(datasourceId.equals("US1")){
			userDAO.setSessionFactory(sessionFactoryUS1);
		} else if(datasourceId.equals("EU1")){
			userDAO.setSessionFactory(sessionFactoryEU1);
		}
	}


	private void useDefaultMultitenantSessionFactory() {
		userDAO.setSessionFactory(sessionFactory);//default
	}
	
	
	@Test//without TM it works?!
	public void testProvisioningWithoutTM() throws SQLException{
		useDefaultMultitenantSessionFactory();
		//manually create a proxy over userService class
		//in order to obtain the last user created 
		UserServiceProxy userServiceInspectProxy = new UserServiceProxy(userService);		
		//second service fails; user shouldn't be inserted in DB
		contentService.setFail(true);
		provisioningService.setUserService(userServiceInspectProxy);
		try{
			provisioningService.allocateAccount();
			fail("Allocalte account should have thrown a RuntimeException!");
		} catch (RuntimeException e){
			//expected
		}
		User u = userServiceInspectProxy.getLastUserCreated();//last user created

		
		
		//check it was not persisted!
		assertThat(userService.loadUser(u.getId()), is(u));//should be null!
		
		useSimpleDatasourceSessionFactory("US1");
		assertThat(userService.loadUser(u.getId()),  is(u));//should be null!
		
		useSimpleDatasourceSessionFactory("EU1");
		assertThat(userService.loadUser(u.getId()), nullValue());
		
		
		//WHY???
		assertThat(dataSourceUS1.getConnection().getAutoCommit(), is(true));
	}

	
	
	@Test
	public void testProvisioningWithTM(){
		useDefaultMultitenantSessionFactory();
		//manually create a proxy over userService class
		//in order to obtain the last user created 
		UserServiceProxy userServiceInspectProxy = new UserServiceProxy(userService);		
		//second service fails; user shouldn't be inserted in DB
		contentService.setFail(true);
		provisioningService.setUserService(userServiceInspectProxy);
		
		
		//do in transaction!
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setAutodetectDataSource(false);		
		transactionManager.setSessionFactory(sessionFactory);
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

		//check it was not persisted!
		assertThat(userService.loadUser(u.getId()), nullValue());
		
		useSimpleDatasourceSessionFactory("US1");
		assertThat(userService.loadUser(u.getId()),  nullValue());
		
		useSimpleDatasourceSessionFactory("EU1");
		assertThat(userService.loadUser(u.getId()), nullValue());
		
	}
}
