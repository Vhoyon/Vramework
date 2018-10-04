package io.github.vhoyon.utilities.sanitizers;

import io.github.vhoyon.exceptions.BadFormatException;
import io.github.vhoyon.modules.Environment;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public interface EnumSanitizer {
	
	static String sanitizeValue(Object value, ArrayList<String> values)
			throws BadFormatException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(!values.contains(stringValue)){
			throw new BadFormatException("The value " + stringValue
					+ " is not a choice for this setting!", 1);
		}
		
		return stringValue;
		
	}
	
	static ArrayList<String> formatEnvironment(String envKey)
			throws BadFormatException{
		return extractEnumFromString(Environment.getVar(envKey));
	}
	
	static ArrayList<String> extractEnumFromString(String stringValue)
			throws BadFormatException{
		return extractEnumFromString(stringValue, '|');
	}
	
	static ArrayList<String> extractEnumFromString(String stringValue,
			char separator) throws BadFormatException{
		
		// Verify that string is of format "[...]| [...] | [...]" while allowing single choice enums.
		// ~ Resetting stringValue here was not necessary, but this will make it future-proof ~
		stringValue = verifyStringFormat(stringValue, separator);
		
		String pSep = protectSeparator(separator);
		
		if(stringValue.matches("^\\s*(\\\\*" + pSep + ")+\\s*$")){
			
			ArrayList<String> values = new ArrayList<>();
			values.add(stringValue);
			
			return values;
			
		}
		else{
			
			String expNonProtectedSeparator = "(?<!\\\\)" + pSep;
			
			String[] possibleValues = stringValue.trim().split(
					"\\s*" + expNonProtectedSeparator + "\\s*");
			
			ArrayList<String> values = new ArrayList<>();
			
			for(String possibleValue : possibleValues){
				values.add(possibleValue.replaceAll("\\\\" + pSep,
						String.valueOf(separator)));
			}
			
			// Remove duplicate while keeping the order of the values
			LinkedHashSet<String> hs = new LinkedHashSet<>();
			hs.addAll(values);
			values.clear();
			values.addAll(hs);
			
			return values;
			
		}
		
	}
	
	static String verifyStringFormat(String stringValue, char separator)
			throws BadFormatException{
		
		// Please see https://regex101.com/r/FrVwfk for an interactive testing session for this regex.
		// Make sure to use the latest version on this website (click the v1 button to check).
		
		String pSep = protectSeparator(separator);
		
		String expAnyNonBreakOrSep = "[^\\n" + pSep + "]*";
		String expAnyNonSpaceOrSep = "([^\\r\\n\\t\\f\\v " + pSep + "]|\\\\"
				+ pSep + ")";
		
		String expOnlySeparators = pSep + "+";
		
		String expValidWord = expAnyNonBreakOrSep + expAnyNonSpaceOrSep
				+ expAnyNonBreakOrSep;
		String expAdditionalValidWords = pSep + expValidWord;
		
		String expValidWordsAsEnum = expValidWord + "("
				+ expAdditionalValidWords + ")*";
		
		String expEnumFormat = expOnlySeparators + "|" + expValidWordsAsEnum;
		
		return TextRegexSanitizer.sanitizeValue(stringValue, expEnumFormat);
		
	}
	
	static String protectSeparator(char separator){
		
		if("<([{\\^-=$!|]})?*+.>".indexOf(separator) != -1){
			return String.format("\\%s", separator);
		}
		else{
			return String.valueOf(separator);
		}
		
	}
	
}
