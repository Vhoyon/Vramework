package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.objects.Dictionary;

public abstract class Translatable implements io.github.vhoyon.vramework.interfaces.Translatable {
	
	private Dictionary dict;
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	public Dictionary getDictionary(){
		return this.dict;
	}
	
}
