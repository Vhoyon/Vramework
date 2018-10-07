package io.github.vhoyon.vramework.utilities.sanitizers;

import io.github.vhoyon.vramework.exceptions.BadFormatException;

public interface TextLengthSanitizer {
	
	static String sanitizeValueMin(Object value, int minLength)
			throws BadFormatException{
		return TextLengthSanitizer.sanitizeValue(value, minLength,
				Integer.MAX_VALUE);
	}
	
	static String sanitizeValueMax(Object value, int maxLength)
			throws BadFormatException{
		return TextLengthSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				maxLength);
	}
	
	static String sanitizeValue(Object value, int minLength, int maxLength)
			throws BadFormatException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		int stringLength = stringValue.length();
		
		if(minLength != Integer.MIN_VALUE && stringLength < minLength){
			throw new BadFormatException(
					"This setting's value needs to have at least " + minLength
							+ " characters!", 1);
		}
		else if(maxLength != Integer.MAX_VALUE && stringLength > maxLength){
			throw new BadFormatException(
					"This setting's value cannot have more than " + maxLength
							+ " characters!", 2);
		}
		
		return stringValue;
		
	}
	
}
