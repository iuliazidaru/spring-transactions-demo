package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.User;

/**
 * A service. Implementation classes will call other services.
 * 
 * @author Iulia
 *
 */
public interface ProvisioningService {

	public User allocateAccount();
	
	public void setContentService(ContentService contentService);

	public void setUserService(UserService userService);
}
