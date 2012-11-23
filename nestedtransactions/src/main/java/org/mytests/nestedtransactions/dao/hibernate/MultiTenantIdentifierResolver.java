package org.mytests.nestedtransactions.dao.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * REsolves the datasource which has to be used.
 * 
 * @author Iulia
 *
 */
public class MultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver  {

	@Override
	public String resolveCurrentTenantIdentifier() {
		return "US1";
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		// TODO Auto-generated method stub
		return false;
	}

}
