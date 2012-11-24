package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.Collection;

public interface ContentService {
	
	public Collection createCollection(Collection c);
		
	public void setFail(boolean fail);

	public Collection loadCollection(Long id);
}
