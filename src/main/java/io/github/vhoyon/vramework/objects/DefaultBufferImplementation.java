package io.github.vhoyon.vramework.objects;

import java.util.HashMap;

import io.github.vhoyon.vramework.interfaces.BufferImplementation;

public class DefaultBufferImplementation implements
		BufferImplementation<HashMap<String, Object>> {
	
	private HashMap<String, Object> memory;
	
	@Override
	public HashMap<String, Object> getMemory(){
		if(this.memory == null)
			this.memory = new HashMap<>();
		
		return this.memory;
	}
	
	@Override
	public boolean store(String key, Object object){
		boolean isNewObject = this.has(key);
		
		this.getMemory().put(key, object);
		
		return isNewObject;
	}
	
	@Override
	public Object retrieve(String key) throws IllegalStateException{
		boolean hasObject = this.has(key);
		
		if(!hasObject){
			throw new IllegalStateException();
		}
		else{
			return this.getMemory().get(key);
		}
	}
	
	@Override
	public boolean has(String key){
		return this.getMemory().containsKey(key);
	}
	
	@Override
	public boolean remove(String key){
		boolean hasRemovedObject = this.getMemory().containsKey(key);
		
		this.getMemory().remove(key);
		
		return hasRemovedObject;
	}
	
	@Override
	public void empty(){
		this.memory = null;
	}
	
}
