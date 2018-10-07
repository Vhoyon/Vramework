package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.utilities.sanitizers.BooleanSanitizer;

public class BooleanField extends SettingField<Boolean> {
	
	public BooleanField(String name, String env, Boolean defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Boolean sanitizeValue(Object value)
			throws IllegalArgumentException{
		return BooleanSanitizer.sanitizeValue(value);
	}
	
}
