package org.mytests.nestedtransactions.dao;

import org.mytests.nestedtransactions.model.Collection;
import org.mytests.nestedtransactions.model.HibCollection;

public class CollectionDaoImpl extends AbstractDaoImpl implements CollectionDao{

	@Override
	public long createCollection(Collection c) {
		return Long.parseLong(getSession().save(c).toString());		
		
	}

	@Override
	public Collection loadCollection(long id) {
		return (Collection)getSession().byId(HibCollection.class).load(id);		
	}

}
