package io.github.vhoyon.vramework.objects;

import java.io.Serializable;

import io.github.vhoyon.vramework.abstracts.Storage;
import io.github.vhoyon.vramework.interfaces.StorageImplementation;

public class Database extends
		Storage<StorageImplementation<Serializable>, Serializable> {
	
	@Override
	protected StorageImplementation<Serializable> getDefaultImplementation(){
		return new DefaultDatabaseImplementation();
	}
	
}
