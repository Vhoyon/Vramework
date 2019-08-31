package io.github.vhoyon.vramework.util.settings;

import io.github.ved.jsanitizers.CharSanitizer;

public class CharField extends Setting<Character> {
	
	public CharField(String name, String env, char defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Character sanitizeValue(Object value)
			throws IllegalArgumentException{
		return CharSanitizer.sanitizeValue(value);
	}
	
}
