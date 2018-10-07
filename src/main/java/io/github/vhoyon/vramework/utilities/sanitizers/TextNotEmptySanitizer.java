package io.github.vhoyon.vramework.utilities.sanitizers;

import io.github.vhoyon.vramework.exceptions.BadFormatException;

public interface TextNotEmptySanitizer {
	
	static String sanitizeValue(Object value) throws BadFormatException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(stringValue.length() == 0){
			throw new BadFormatException("Value cannot be empty!", 1);
		}
		
		return stringValue;
		
	}
	
}
