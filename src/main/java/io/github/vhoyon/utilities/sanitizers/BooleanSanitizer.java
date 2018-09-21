package io.github.vhoyon.utilities.sanitizers;

import io.github.vhoyon.exceptions.BadFormatException;

public interface BooleanSanitizer {
	
	static boolean sanitizeValue(Object value) throws BadFormatException{
		
		boolean castedValue;
		
		try{
			
			if(value instanceof String){
				
				String stringValue = (String)value;
				
				if(!stringValue.matches("^(?i)true|false$")){
					throw new Exception();
				}
				
				castedValue = Boolean.valueOf(stringValue);
				
			}
			else{
				
				castedValue = (boolean)value;
				
			}
			
		}
		catch(Exception e){
			throw new BadFormatException(
					"Value cannot be something else than \"true\" or \"false\"!", 1);
		}
		
		return castedValue;
		
	}
	
}
