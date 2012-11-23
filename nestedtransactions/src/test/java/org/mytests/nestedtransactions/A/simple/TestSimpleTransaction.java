package org.mytests.nestedtransactions.A.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mytests.nestedtransactions.model.HibUser;
import org.mytests.nestedtransactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestSimpleTransaction {
	@Autowired
	private UserService userService;
	
	@Test
	public void testSimpleTransaction(){
		//see log!!
		userService.createUser(new HibUser());
	}
	
	
	@Test(expected=RuntimeException.class)
	public void testSimpleFailingTransaction(){
		//see log!!
		userService.setFail(true);
		userService.createUser(new HibUser());
	}
	
	
}
