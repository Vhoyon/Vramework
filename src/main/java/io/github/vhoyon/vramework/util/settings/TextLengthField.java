package io.github.vhoyon.vramework.util.settings;

import io.github.ved.jsanitizers.TextLengthSanitizer;

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
	
	@Override
	protected TextLengthField clone() throws CloneNotSupportedException{
		TextLengthField cloned = new TextLengthField(getName(), getEnv(),
				getDefaultValue(), this.minLength, this.maxLength){
			@Override
			protected String sanitizeValue(Object value)
					throws IllegalArgumentException{
				return TextLengthField.this.sanitizeValue(value);
			}
		};
		
		return cloned;
	}
	
}
