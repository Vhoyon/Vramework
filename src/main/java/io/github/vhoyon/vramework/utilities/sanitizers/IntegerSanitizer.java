package io.github.vhoyon.vramework.utilities.sanitizers;

public interface IntegerSanitizer {
	
	static int sanitizeValue(Object value) throws IllegalArgumentException{
		return IntegerSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}
	
	static int sanitizeValueMin(Object value, int minValue)
			throws IllegalArgumentException{
		return IntegerSanitizer.sanitizeValue(value, minValue,
				Integer.MAX_VALUE);
	}
	
	static int sanitizeValueMax(Object value, int maxValue)
			throws IllegalArgumentException{
		return IntegerSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				maxValue);
	}
	
	static int sanitizeValue(Object value, int minValue, int maxValue)
			throws IllegalArgumentException{
		
		int castedValue;
		
		try{
			
			if(value instanceof String){
				castedValue = Integer.valueOf((String)value);
			}
			else{
				castedValue = (Integer)value;
			}
			
		}
		catch(Exception e){
			throw new NumberFormatException("Value is not a number!");
		}
		
		if(minValue != Integer.MIN_VALUE && castedValue < minValue){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is lower than the minimum required (" + minValue
					+ ")!");
		}
		else if(maxValue != Integer.MAX_VALUE && castedValue > maxValue){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is higher than the maximum permitted (" + maxValue
					+ ")!");
		}
		
		return castedValue;
		
	}
	
}
