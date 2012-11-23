package org.mytests.nestedtransactions.dao;

import java.util.Set;

import org.mytests.nestedtransactions.model.Collection;


public interface CollectionDao {

	public void createCollection(Collection c);
	
	public Set<Collection> loadCollection();
}
