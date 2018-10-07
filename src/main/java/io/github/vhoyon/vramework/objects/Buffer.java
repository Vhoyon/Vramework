package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.interfaces.Utils;

import java.util.HashMap;

public class Buffer {
	
	private static Buffer buffer;
	
	private HashMap<String, Object> memory;
	
	private Buffer(){
		memory = new HashMap<>();
	}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	public boolean push(Object object, String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return this.push(object, objectKey);
		
	}
	
	public boolean push(Object object, String fullKey){
		
		boolean isNewObject = !memory.containsKey(fullKey);
		
		memory.put(fullKey, object);
		
		return isNewObject;
		
	}
	
	public Object get(String associatedName, String key)
			throws NullPointerException{
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return this.get(objectKey);
		
	}
	
	public Object get(String fullKey) throws NullPointerException{
		
		boolean hasObject = memory.containsKey(fullKey);
		
		if(!hasObject){
			throw new NullPointerException("No object with the key \""
					+ fullKey + "\" was found in the Buffer.");
		}
		else{
			
			Object memoryObject = memory.get(fullKey);
			
			return memoryObject;
			
		}
		
	}
	
	public boolean remove(String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		boolean hasRemovedObject = memory.containsKey(objectKey);
		
		memory.remove(objectKey);
		
		return hasRemovedObject;
		
	}
	
	public void emptyMemory(){
		memory = new HashMap<>();
	}
	
	public boolean has(String key){
		return this.memory.containsKey(key);
	}
	
}
