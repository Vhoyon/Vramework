package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.interfaces.StorageImplementation;

import java.io.Serializable;

public class DefaultDatabaseImplementation implements StorageImplementation<Serializable> {
	
	public static final String DEFAULT_TABLE_NAME = "VHOYON_GENERIC";
	
	@Override
	public boolean store(String key, Serializable object){
		return this.store(DEFAULT_TABLE_NAME, key, object);
	}
	
	@Override
	public Serializable retrieve(String key) throws IllegalStateException{
		return this.retrieve(DEFAULT_TABLE_NAME, key);
	}
	
	@Override
	public boolean has(String key){
		return this.has(DEFAULT_TABLE_NAME, key);
	}
	
	@Override
	public boolean remove(String key){
		return this.remove(DEFAULT_TABLE_NAME, key);
	}
	
	@Override
	public void clear(){
		this.clear(DEFAULT_TABLE_NAME);
	}
	
	public boolean store(String tableName, String key, Serializable object){
		return false;
	}
	
	public Serializable retrieve(String tableName, String key)
			throws IllegalStateException{
		return null;
	}
	
	public boolean has(String tableName, String key){
		return false;
	}
	
	public boolean remove(String tableName, String key){
		return false;
	}
	
	public void clear(String tableName){
		
	}
	
}
