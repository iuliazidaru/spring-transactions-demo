package org.mytests.nestedtransactions.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Iulia
 *
 */
@Entity
@Table(name="collection")
public class HibCollection implements Collection{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id = Long.valueOf(-1);
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
