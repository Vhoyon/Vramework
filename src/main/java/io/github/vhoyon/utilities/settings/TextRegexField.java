package io.github.vhoyon.utilities.settings;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import io.github.vhoyon.modules.Logger;
import io.github.vhoyon.modules.Logger.LogType;
import io.github.vhoyon.utilities.sanitizers.TextRegexSanitizer;

public class TextRegexField extends TextField {
	
	private String regexToMatch;
	private boolean isInverted;
	private boolean shouldBox;
	
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
			Logger.log("\nFix your regex quickly! In the meanwhile, any string will be accepted for the setting \"" + getName() + "\"...", LogType.WARNING);
			
			this.regexToMatch = null;
			
		}
		
		this.isInverted = isInverted;
		this.shouldBox = shouldBox;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextRegexSanitizer.sanitizeValue(value, this.regexToMatch,
				this.isInverted, this.shouldBox, false);
	}
	
}
