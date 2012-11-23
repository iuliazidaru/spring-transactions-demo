package org.mytests.nestedtransactions.dao;

import java.util.Set;

import org.mytests.nestedtransactions.model.Collection;

public class CollectionDaoImpl extends AbstractDaoImpl implements CollectionDao{

	@Override
	public void createCollection(Collection c) {
		getSession().save(c);
		
	}

	@Override
	public Set<Collection> loadCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
