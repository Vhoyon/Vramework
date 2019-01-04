package io.github.vhoyon.vramework.objects;

import java.util.HashMap;
import java.util.concurrent.Callable;

import io.github.vhoyon.vramework.interfaces.Utils;

public class Buffer {
	
	private static Buffer buffer;
	
	private HashMap<String, Object> memory;
	private HashMap<Class<?>, Object> singletonMemory;
	
	private Buffer(){
		memory = new HashMap<>();
		singletonMemory = new HashMap<>();
	}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	public static <E> E getSingleton(Class<E> desiredClass,
			Callable<E> createInstanceIfNew)
			throws UnsupportedOperationException{
		
		if(desiredClass == null){
			throw new IllegalArgumentException(
					"The desiredClass parameter cannot be null!");
		}
		
		Buffer buffer = Buffer.get();
		
		if(buffer.singletonMemory.containsKey(desiredClass)){
			return (E)buffer.singletonMemory.get(desiredClass);
		}
		else{
			
			E newClassSingleton = null;
			
			try{
				newClassSingleton = createInstanceIfNew.call();
			}
			catch(Exception e){
				throw new UnsupportedOperationException(
						"The code provided to create the new singleton instance threw an Exception.",
						e);
			}
			
			buffer.singletonMemory.put(desiredClass, newClassSingleton);
			
			return newClassSingleton;
			
		}
		
	}
	
	public static <E> E removeSingleton(Class<E> singletonClass){
		return (E)Buffer.get().singletonMemory.remove(singletonClass);
	}
	
	public static boolean hasSingleton(Class<?> singletonClass){
		return Buffer.get().singletonMemory.containsKey(singletonClass);
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
			return memory.get(fullKey);
		}
		
	}
	
	public boolean remove(String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return remove(objectKey);
		
	}
	
	public boolean remove(String fullKey){
		
		boolean hasRemovedObject = memory.containsKey(fullKey);
		
		memory.remove(fullKey);
		
		return hasRemovedObject;
		
	}
	
	public void emptyMemory(){
		memory = new HashMap<>();
	}
	
	public boolean has(String key){
		return this.memory.containsKey(key);
	}
	
}
