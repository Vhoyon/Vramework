package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.interfaces.StorageImplementation;

public abstract class Storage<E extends StorageImplementation<S>, S> {
	
	private StorageImplementation<S> storageImpl;
	
	protected Storage(){}
	
	protected E getMemoryImpl(){
		if(this.storageImpl == null)
			this.setMemoryImplementation(this.getDefaultImplementation());
		//noinspection unchecked
		return (E)this.storageImpl;
	}
	
	public void setMemoryImplementation(
			StorageImplementation<S> implementation){
		this.storageImpl = implementation;
	}
	
	protected abstract E getDefaultImplementation();
	
	public boolean store(S object, String fullKey){
		return this.getMemoryImpl().store(fullKey, object);
	}
	
	public <T> T retrieve(String fullKey) throws IllegalStateException{
		try{
			//noinspection unchecked
			return (T)this.getMemoryImpl().retrieve(fullKey);
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
