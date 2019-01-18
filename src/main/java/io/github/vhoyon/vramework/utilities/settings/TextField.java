package io.github.vhoyon.vramework.utilities.settings;

import io.github.ved.jsanitizers.TextSanitizer;

public class TextField extends Setting<String> {
	
	public TextField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextSanitizer.sanitizeValue(value);
	}
	
}
