package io.github.vhoyon.vramework.utilities.sanitizers;

import io.github.vhoyon.vramework.exceptions.BadFormatException;

public interface IntegerSanitizer {
	
	static int sanitizeValue(Object value) throws BadFormatException{
		return IntegerSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}
	
	static int sanitizeValueMin(Object value, int minValue)
			throws BadFormatException{
		return IntegerSanitizer.sanitizeValue(value, minValue,
				Integer.MAX_VALUE);
	}
	
	static int sanitizeValueMax(Object value, int maxValue)
			throws BadFormatException{
		return IntegerSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				maxValue);
	}
	
	static int sanitizeValue(Object value, int minValue, int maxValue)
			throws BadFormatException{
		
		int castedValue;
		
		try{
			
			if(value instanceof String){
				castedValue = Integer.valueOf(TextNotEmptySanitizer
						.sanitizeValue(value));
			}
			else{
				castedValue = (Integer)value;
			}
			
		}
		catch(ClassCastException | NumberFormatException e){
			throw new BadFormatException("Value is not a number!", 2);
		}
		
		if(minValue != Integer.MIN_VALUE && castedValue < minValue){
			throw new BadFormatException("Value (" + castedValue
					+ ") is lower than the minimum required (" + minValue
					+ ")!", 3);
		}
		else if(maxValue != Integer.MAX_VALUE && castedValue > maxValue){
			throw new BadFormatException("Value (" + castedValue
					+ ") is higher than the maximum permitted (" + maxValue
					+ ")!", 4);
		}
		
		return castedValue;
		
	}
	
}
