package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.Collection;

public interface ContentService {
	
	public void createCollection(Collection c);
	
	public Collection loadCollection();
	
	public void setFail(boolean fail);
}
