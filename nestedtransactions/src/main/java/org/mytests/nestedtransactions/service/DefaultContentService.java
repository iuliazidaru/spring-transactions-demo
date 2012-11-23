package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.Collection;

public class DefaultContentService implements ContentService{
	private boolean fail = false;
	
	public void createCollection(Collection c) {
		System.out.println("DefaultContentService.createCollection()");
		if(fail){
			throw new RuntimeException();
		}		
		
	}

	@Override
	public Collection loadCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFail(boolean fail) {
		this.fail = fail;
	}
}
