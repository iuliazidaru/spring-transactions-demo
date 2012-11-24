package org.mytests.nestedtransactions.service;

import org.mytests.nestedtransactions.model.Collection;

public class DefaultContentService implements ContentService{
	private boolean fail = false;
	
	public Collection createCollection(Collection c) {
		System.out.println("DefaultContentService.createCollection()");
		if(fail){
			throw new RuntimeException();
		}
		
		return c;		
	}

	

	@Override
	public void setFail(boolean fail) {
		this.fail = fail;
	}



	


	@Override
	public Collection loadCollection(Long id) {
		return null;
	}
}
