package io.github.vhoyon.vramework.interfaces;

public interface StorageImplementation<E> {

	boolean store(String key, E object);

	E retrieve(String key) throws IllegalStateException;

	boolean has(String key);

	boolean remove(String key);
	
	void clear();
	
}
