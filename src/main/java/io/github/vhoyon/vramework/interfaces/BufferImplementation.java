package io.github.vhoyon.vramework.interfaces;

public interface BufferImplementation<E> {
	
	E getMemory();
	
	boolean store(String key, Object object);
	
	Object retrieve(String key) throws IllegalStateException;
	
	boolean has(String key);
	
	boolean remove(String key);
	
	void empty();
	
}
