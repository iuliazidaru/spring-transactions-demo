package org.mytests.nestedtransactions.dao;

import org.mytests.nestedtransactions.model.Collection;


public interface CollectionDao {

	public long createCollection(Collection c);
	
	public Collection loadCollection(long id);
}
