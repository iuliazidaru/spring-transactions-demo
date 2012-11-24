package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.Collection;
import org.mytests.nestedtransactions.model.HibCollection;
import org.mytests.nestedtransactions.model.HibUser;
import org.mytests.nestedtransactions.model.User;

/**
 * Test implementation which calls 2 services.
 * 
 * @author Iulia
 *
 */
public class DefaultProvisioningService implements ProvisioningService{

	private ContentService contentService;
	
	private UserService userService;
	
	@Override
	public User allocateAccount() {
		User u = userService.createUser(new HibUser());
		Collection c = contentService.createCollection(new HibCollection());
		u.setCollection(c);
		return u;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
