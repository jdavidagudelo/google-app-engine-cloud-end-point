package com.artica.udea.endpoint.hello;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
@PersistenceCapable
public class PersistentClass {
	@Persistent()
	private String name;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public PersistentClass(String name) {
		super();
		this.name = name;
	}
	
}
