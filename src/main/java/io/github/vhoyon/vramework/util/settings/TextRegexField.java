package io.github.vhoyon.vramework.util.settings;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import io.github.ved.jsanitizers.TextRegexSanitizer;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Logger.LogType;

public class TextRegexField extends TextField {
	
	protected String regexToMatch;
	protected boolean isInverted;
	protected boolean shouldBox;
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch){
		this(name, env, defaultValue, regexToMatch, false);
	}
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch, boolean isInverted){
		this(name, env, defaultValue, regexToMatch, isInverted, true);
	}
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch, boolean isInverted, boolean shouldBox){
		super(name, env, defaultValue);
		
		try{
			// Test if Regex provided is valid
			Pattern.compile(regexToMatch);
		}
		catch(PatternSyntaxException e){
			
			Logger.log(e);
			Logger.log(
					"\nFix your regex quickly! In the meanwhile, any string will be accepted for the setting \""
							+ getName() + "\"...", LogType.WARNING);
			
			this.regexToMatch = null;
			
		}
		
		this.isInverted = isInverted;
		this.shouldBox = shouldBox;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextRegexSanitizer.sanitizeValue(value, this.regexToMatch,
				this.isInverted, this.shouldBox);
	}
	
	@Override
	protected TextRegexField clone() throws CloneNotSupportedException{
		TextRegexField cloned = new TextRegexField(getName(), getEnv(),
				getDefaultValue(), this.regexToMatch, this.isInverted,
				this.shouldBox){
			@Override
			protected String sanitizeValue(Object value)
					throws IllegalArgumentException{
				return TextRegexField.this.sanitizeValue(value);
			}
		};
		
		return cloned;
	}
	
}
