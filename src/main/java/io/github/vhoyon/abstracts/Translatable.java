package io.github.vhoyon.abstracts;

import io.github.vhoyon.objects.Dictionary;

public abstract class Translatable implements io.github.vhoyon.interfaces.Translatable {
	
	private Dictionary dict;
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	public Dictionary getDictionary(){
		return this.dict;
	}
	
}
