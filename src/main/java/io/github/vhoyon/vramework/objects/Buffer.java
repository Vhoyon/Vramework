package io.github.vhoyon.vramework.objects;

import java.util.concurrent.Callable;

import io.github.vhoyon.vramework.abstracts.Storage;
import io.github.vhoyon.vramework.interfaces.BufferImplementation;
import io.github.vhoyon.vramework.interfaces.StorageImplementation;

public class Buffer extends Storage {
	
	private static Buffer buffer;
	
	private BufferImplementation<?> singletonImpl;
	
	private Buffer(){}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	@Override
	protected StorageImplementation getDefaultImplementation(){
		return new DefaultBufferImplementation();
	}
	
	protected final BufferImplementation<?> getSingletonMemoryImpl(){
		if(this.singletonImpl == null)
			Buffer.setSingletonMemoryImplementation(new DefaultBufferImplementation());
		
		return this.singletonImpl;
	}
	
	public static void setSingletonMemoryImplementation(
			BufferImplementation<?> implementation){
		Buffer.get().singletonImpl = implementation;
	}
	
	public static void setImplementation(BufferImplementation<?> implementation){
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
	
	public void emptySingletonMemory(){
		this.getSingletonMemoryImpl().clear();
	}
	
	public void emptyAllMemory(){
		this.clearMemory();
		this.emptySingletonMemory();
	}
	
}
