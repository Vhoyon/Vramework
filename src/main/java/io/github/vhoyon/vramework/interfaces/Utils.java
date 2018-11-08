package io.github.vhoyon.vramework.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.vhoyon.vramework.Framework;
import io.github.vhoyon.vramework.modules.Environment;

public interface Utils {
	
	default String formatS(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	default String format(String stringToFormat, Object... replacements){
		
		String protectedString = stringToFormat.replaceAll("(%)", "$1$1");
		
		String noLeadingZeroString = protectedString.replaceAll(
				"\\{0+([1-9][0-9]*)", "\\{$1");
		
		String cleaned = noLeadingZeroString.replaceAll("\\{([1-9][0-9]*)\\}",
				"\\%$1\\$s");
		
		String cleanedProtected = cleaned.replaceAll(
				"\\{(\\\\*)\\\\(0*[1-9][0-9]*)}", "\\{$1$2\\}");
		
		return String.format(cleanedProtected, replacements);
		
	}
	
	default boolean isDebugging(){
		return env("DEBUG", false) || Framework.isDebugging();
	}
	
	default <EnvVar> EnvVar env(String key, EnvVar defaultValue){
		return Environment.getVar(key, defaultValue);
	}
	
	default <EnvVar> EnvVar env(String key){
		return env(key, null);
	}
	
	default boolean hasEnv(String key){
		return Environment.hasVar(key);
	}
	
	static String buildKey(String baseKey, String... additionalKeys){
		StringBuilder keyBuilder = new StringBuilder(baseKey);
		
		for(String additionalKey : additionalKeys)
			keyBuilder.append("_").append(additionalKey);
		
		return keyBuilder.toString();
	}
	
	default ArrayList<String> splitSpacesExcludeQuotes(String string){
		ArrayList<String> possibleStrings = new ArrayList<>();
		Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'")
				.matcher(string);
		while(matcher.find()){
			possibleStrings.add(matcher.group());
		}
		
		return possibleStrings;
	}
	
	default ArrayList<String> splitSpacesExcludeQuotesMaxed(String string,
			int maxSize){
		
		if(maxSize == 0){
			ArrayList<String> singleEntry = new ArrayList<>();
			singleEntry.add(string);
			
			return singleEntry;
		}
		
		ArrayList<String> fullSplit = splitSpacesExcludeQuotes(string);
		
		if(maxSize < 0 || fullSplit.size() <= maxSize){
			return fullSplit;
		}
		
		List<String> splittedUpTo = fullSplit.subList(0, maxSize - 1);
		List<String> allOthers = fullSplit.subList(maxSize,
				fullSplit.size() - 1);
		
		ArrayList<String> allAndTruncated = new ArrayList<>();
		allAndTruncated.addAll(splittedUpTo);
		allAndTruncated.add(String.join(" ", allOthers));
		
		return allAndTruncated;
	}
	
}
