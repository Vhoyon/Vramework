package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.utilities.sanitizers.CharSanitizer;

public class CharField extends SettingField<Character> {
	
	public CharField(String name, String env, char defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Character sanitizeValue(Object value)
			throws IllegalArgumentException{
		return CharSanitizer.sanitizeValue(value);
	}
	
}
