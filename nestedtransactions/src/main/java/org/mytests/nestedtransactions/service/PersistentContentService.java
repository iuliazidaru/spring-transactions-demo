package org.mytests.nestedtransactions.service;


import org.mytests.nestedtransactions.dao.CollectionDao;
import org.mytests.nestedtransactions.model.Collection;

public class PersistentContentService implements ContentService{
	private CollectionDao collectionDao;
	
	private boolean fail = false;
	
	@Override
	public Collection createCollection(Collection c) {
		if(fail){
			throw new RuntimeException();
		}
		long id = collectionDao.createCollection(c);	
		c.setId(id);
		return c;		
	}

	@Override
	public void setFail(boolean fail) {	
		this.fail = fail;
	}

	@Override
	public Collection loadCollection(Long id) {
		return collectionDao.loadCollection(id);
	}

	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}
	
	

}
