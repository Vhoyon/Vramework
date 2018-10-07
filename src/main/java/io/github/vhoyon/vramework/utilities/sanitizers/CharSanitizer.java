package io.github.vhoyon.vramework.utilities.sanitizers;

import io.github.vhoyon.vramework.exceptions.BadFormatException;

public interface CharSanitizer {
	
	static char sanitizeValue(Object value) throws BadFormatException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(stringValue.length() != 1){
			throw new BadFormatException(
					"Only one character is expected!", 1);
		}
		
		return stringValue.charAt(0);
		
	}
	
}
