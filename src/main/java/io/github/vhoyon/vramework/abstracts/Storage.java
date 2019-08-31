package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.interfaces.StorageImplementation;

public abstract class Storage {
	
	private static StorageImplementation storageImpl;
	
	protected Storage(){}
	
	protected StorageImplementation getMemoryImpl(){
		if(Storage.storageImpl == null)
			Storage.setMemoryImplementation(this.getDefaultImplementation());
		
		return Storage.storageImpl;
	}
	
	public static void setMemoryImplementation(
			StorageImplementation implementation){
		Storage.storageImpl = implementation;
	}
	
	protected abstract StorageImplementation getDefaultImplementation();
	
	public boolean store(Object object, String fullKey){
		return this.getMemoryImpl().store(fullKey, object);
	}
	
	public <E> E retrieve(String fullKey) throws IllegalStateException{
		try{
			//noinspection unchecked
			return (E)this.getMemoryImpl().retrieve(fullKey);
		}
		catch(IllegalStateException e){
			throw new IllegalStateException("No object with the key \""
					+ fullKey + "\" was found in the Buffer.");
		}
	}
	
	public boolean has(String key){
		return this.getMemoryImpl().has(key);
	}
	
	public boolean remove(String fullKey){
		return this.getMemoryImpl().remove(fullKey);
	}
	
	public void clearMemory(){
		this.getMemoryImpl().clear();
	}
	
}
