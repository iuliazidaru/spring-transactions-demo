package org.mytests.nestedtransactions.B.nested;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mytests.nestedtransactions.service.ContentService;
import org.mytests.nestedtransactions.service.ProvisioningService;
import org.mytests.nestedtransactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestNestedTransaction {
	@Autowired
	private ProvisioningService provisioningService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentService contentService;
	
	
	@Test
	public void testSimpleTransaction(){
		//see log!!
		userService.setFail(false);
		contentService.setFail(false);
		provisioningService.allocateAccount();
	}
	
	
	@Test(expected=RuntimeException.class)
	public void testSimpleFailingTransaction(){
		//see log!!
		userService.setFail(true);
		contentService.setFail(false);
		provisioningService.allocateAccount();
	}
	
	@Test(expected=RuntimeException.class)
	public void testSimpleFailingTransaction2(){
		//see log!!
		userService.setFail(false);
		contentService.setFail(true);
		provisioningService.allocateAccount();
	}
	
	
}
