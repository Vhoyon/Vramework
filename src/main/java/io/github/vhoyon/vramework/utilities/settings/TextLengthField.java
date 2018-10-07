package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.utilities.sanitizers.TextLengthSanitizer;

public class TextLengthField extends TextNotEmptyField {
	
	private int minLength;
	private int maxLength;
	
	public TextLengthField(String name, String env, String defaultValue,
			int minLength, int maxLength){
		super(name, env, defaultValue);
		
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextLengthSanitizer.sanitizeValue(value, this.minLength,
				this.maxLength);
	}
	
}
