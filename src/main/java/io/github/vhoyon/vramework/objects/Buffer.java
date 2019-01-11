package io.github.vhoyon.vramework.objects;

import java.util.concurrent.Callable;

import io.github.vhoyon.vramework.interfaces.BufferImplementation;
import io.github.vhoyon.vramework.interfaces.Utils;

public class Buffer {
	
	private static Buffer buffer;
	
	private BufferImplementation memoryImpl;
	private BufferImplementation singletonImpl;
	
	private Buffer(){}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	protected final BufferImplementation getMemoryImpl(){
		if(this.memoryImpl == null)
			setMemoryImplementation(new DefaultBufferImplementation());
		
		return this.memoryImpl;
	}
	
	protected final BufferImplementation getSingletonMemoryImpl(){
		if(this.singletonImpl == null)
			setSingletonMemoryImplementation(new DefaultBufferImplementation());
		
		return this.singletonImpl;
	}
	
	public static void setMemoryImplementation(
			BufferImplementation implementation){
		Buffer.get().memoryImpl = implementation;
	}
	
	public static void setSingletonMemoryImplementation(
			BufferImplementation implementation){
		Buffer.get().singletonImpl = implementation;
	}
	
	public static void setImplementation(BufferImplementation implementation){
		Buffer.setMemoryImplementation(implementation);
		Buffer.setSingletonMemoryImplementation(implementation);
	}
	
	public static <E> E getSingleton(Class<E> desiredClass){
		return getSingleton(desiredClass, null);
	}
	
	public static <E> E getSingleton(Class<E> desiredClass,
			Callable<E> createInstanceIfNew)
			throws UnsupportedOperationException{
		
		if(desiredClass == null){
			throw new IllegalArgumentException(
					"The desiredClass parameter cannot be null!");
		}
		
		String classKey = desiredClass.getName();
		
		Buffer buffer = Buffer.get();
		
		if(buffer.getSingletonMemoryImpl().has(classKey)){
			//noinspection unchecked
			return (E)buffer.getSingletonMemoryImpl().retrieve(classKey);
		}
		else{
			
			if(createInstanceIfNew == null)
				return null;
			
			E newClassSingleton;
			
			try{
				newClassSingleton = createInstanceIfNew.call();
			}
			catch(Exception e){
				throw new UnsupportedOperationException(
						"The code provided to create the new singleton instance threw an Exception.",
						e);
			}
			
			buffer.getSingletonMemoryImpl().store(classKey, newClassSingleton);
			
			return newClassSingleton;
			
		}
		
	}
	
	public static <E> E removeSingleton(Class<E> singletonClass){
		
		Buffer buffer = Buffer.get();
		
		String classKey = singletonClass.getName();
		
		if(!buffer.getSingletonMemoryImpl().has(classKey))
			return null;
		
		//noinspection unchecked
		E singletonInstance = (E)buffer.getSingletonMemoryImpl().retrieve(
				classKey);
		
		buffer.getSingletonMemoryImpl().remove(classKey);
		
		return singletonInstance;
		
	}
	
	public static boolean hasSingleton(Class<?> singletonClass){
		return Buffer.get().getSingletonMemoryImpl()
				.has(singletonClass.getName());
	}
	
	public boolean push(Object object, String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return this.push(object, objectKey);
		
	}
	
	public boolean push(Object object, String fullKey){
		return this.getMemoryImpl().store(fullKey, object);
	}
	
	public <E> E get(String associatedName, String key)
			throws IllegalStateException{
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return this.get(objectKey);
		
	}
	
	public <E> E get(String fullKey) throws IllegalStateException{
		try{
			//noinspection unchecked
			return (E)this.getMemoryImpl().retrieve(fullKey);
		}
		catch(IllegalStateException e){
			throw new IllegalStateException("No object with the key \""
					+ fullKey + "\" was found in the Buffer.");
		}
	}
	
	public boolean remove(String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		return remove(objectKey);
		
	}
	
	public boolean remove(String fullKey){
		return this.getMemoryImpl().remove(fullKey);
	}
	
	public void emptyMemory(){
		this.getMemoryImpl().empty();
	}
	
	public boolean has(String key){
		return this.getMemoryImpl().has(key);
	}
	
}
