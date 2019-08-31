package io.github.vhoyon.vramework.util.settings;

import io.github.ved.jsanitizers.BooleanSanitizer;

public class BooleanField extends Setting<Boolean> {
	
	public BooleanField(String name, String env, Boolean defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Boolean sanitizeValue(Object value)
			throws IllegalArgumentException{
		return BooleanSanitizer.sanitizeValue(value);
	}
	
}
