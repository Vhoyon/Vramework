package io.github.vhoyon.vramework.objects;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import io.github.ved.jsanitizers.exceptions.BadFormatException;
import io.github.vhoyon.vramework.exceptions.AmountNotDefinedException;
import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Logger.LogType;

public class Dictionary implements Utils {
	
	private static final String DEFAULT_LANG = "en";
	private static final String DEFAULT_COUNTRY = "US";
	
	private static final String DEFAULT_DIRECTORY = "lang";
	private static final String DEFAULT_FILE_NAME = "strings";
	
	private static final String ROOT_CHAR = "/";
	
	private ResourceBundle resources;
	private Locale locale;
	private String currentResourcesPath;
	
	private String directory;
	private String fileName;
	
	public Dictionary(){
		this.directory = DEFAULT_DIRECTORY;
		this.fileName = DEFAULT_FILE_NAME;
	}
	
	private String getDirectory(){
		return this.directory;
	}
	
	private void setDirectory(String directory){
		if(!getDirectory().equals(directory))
			this.directory = directory;
	}
	
	private String getFileName(){
		return this.fileName;
	}
	
	private void setFileName(String fileName){
		if(!getFileName().equals(fileName))
			this.fileName = fileName;
	}
	
	private ResourceBundle getResources(){
		if(this.resources == null){
			this.resources = getDefaultLanguageResources();
		}
		
		return this.resources;
	}
	
	private String getResourcePath(){
		String resourcePath = "";
		
		String directory = this.getDirectory();
		
		if(directory != null && directory.length() != 0)
			resourcePath += directory + ".";
		
		resourcePath += getFileName();
		
		return resourcePath;
	}
	
	public void setLanguage(String lang, String country){
		
		if(isDebugging())
			ResourceBundle.clearCache();
		
		locale = new Locale(lang, country);
		this.resources = this.getLanguageResources(locale);
		
	}
	
	public String getDirectString(String key, Object... replacements){
		return this.getString(key, null, replacements);
	}
	
	public String getDirectString(String key){
		return this.getString(key, null);
	}
	
	public String getString(String key, String possiblePrefix,
			Object... replacements){
		if(replacements.length == 1 && replacements[0] == null)
			return this.getString(key, possiblePrefix);
		
		return format(this.getString(key, possiblePrefix), replacements);
	}
	
	public String getString(String key, String possiblePrefix){
		
		if(key == null){
			throw new IllegalArgumentException(
					"The \"key\" parameter cannot be null!");
		}
		
		key = this.handleKey(key);
		
		this.handleResourcesUpdate();
		
		String string = null;
		
		try{
			try{
				
				string = this.tryGetString(this.getResources(), key,
						possiblePrefix);
				
				if(string == null || string.length() == 0)
					throw new NullPointerException();
				
			}
			catch(MissingResourceException e){
				
				string = this.tryGetString(this.getDefaultLanguageResources(),
						key, possiblePrefix);
				
				if(isDebugging())
					Logger.log(
							"Key \""
									+ key
									+ "\" is missing in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
			catch(NullPointerException e){
				
				string = this.tryGetString(this.getDefaultLanguageResources(),
						key, possiblePrefix);
				
				if(isDebugging())
					Logger.log(
							"Key \""
									+ key
									+ "\" is empty in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
		}
		catch(MissingResourceException e){
			
			Logger.log("Key \"" + key
					+ "\" is not in any resource file - what's up with that?",
					LogType.WARNING);
			
		}
		
		return string;
		
	}
	
	public String getStringAmount(String key, String possiblePrefix,
			int amount, Object... replacements) throws BadFormatException,
			AmountNotDefinedException{
		if(replacements.length == 1 && replacements[0] == null)
			return this.getStringAmount(key, possiblePrefix, amount);
		
		return format(this.getStringAmount(key, possiblePrefix, amount),
				replacements);
	}
	
	public String getStringAmount(String key, String possiblePrefix, int amount)
			throws BadFormatException, AmountNotDefinedException{
		
		String langLine = this.getString(key, possiblePrefix);
		
		String message;
		
		if(langLine.matches("^[^|]+\\\\+\\|+[^|]+$")){
			message = langLine;
		}
		else{
			
			LangAmountManager langManager = new LangAmountManager(langLine);
			
			message = langManager.getMessageAmount(amount);
			
		}
		
		String cleaned = message.replaceAll("\\{(0)}", "\\%$1\\$s");
		
		String cleanedProtected = cleaned.replaceAll("\\{(\\\\*)\\\\0}",
				"\\{$1\\0\\}");
		
		return String.format(cleanedProtected, amount);
		
	}
	
	private String handleKey(String key){
		
		if(!key.startsWith(ROOT_CHAR) && !key.contains(".")){
			
			this.setDirectory(DEFAULT_DIRECTORY);
			this.setFileName(DEFAULT_FILE_NAME);
			
			return key;
			
		}
		else{
			
			StringBuilder builder = new StringBuilder();
			
			if(!key.startsWith(ROOT_CHAR)){
				builder.append(DEFAULT_DIRECTORY).append(".");
			}
			else{
				key = key.substring(1);
			}
			
			String simplifiedKey;
			
			String[] structure = key.split("\\.");
			
			if(structure.length == 1){
				
				this.setFileName(DEFAULT_FILE_NAME);
				simplifiedKey = key;
				
			}
			else{
				
				// Don't set the directory structure for either the file name and the key.
				for(int i = 0; i < structure.length - 2; i++){
					
					if(i != 0){
						builder.append(".");
					}
					
					builder.append(structure[i]);
					
				}
				
				this.setFileName(structure[structure.length - 2]);
				
				simplifiedKey = structure[structure.length - 1];
				
			}
			
			this.setDirectory(builder.toString());
			
			return simplifiedKey;
			
		}
		
	}
	
	private String tryGetString(ResourceBundle resources, String key,
			String possiblePrefix) throws MissingResourceException{
		
		String string;
		
		try{
			string = resources.getString(key);
		}
		catch(MissingResourceException e){
			
			if(possiblePrefix == null || possiblePrefix.length() == 0)
				throw e;
			
			string = resources.getString(possiblePrefix + key);
			
		}
		
		return string;
		
	}
	
	private void handleResourcesUpdate(){
		
		String resourcePath = this.getResourcePath();
		
		if(!resourcePath.equals(this.currentResourcesPath)){
			
			this.resources = this.getLanguageResources(this.locale);
			
			this.currentResourcesPath = resourcePath;
			
		}
		
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources(DEFAULT_LANG, DEFAULT_COUNTRY);
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		this.locale = new Locale(lang, country);
		
		return getLanguageResources(this.locale);
	}
	
	private ResourceBundle getLanguageResources(Locale locale){
		if(locale == null){
			return getDefaultLanguageResources();
		}
		else{
			return ResourceBundle.getBundle(this.getResourcePath(), locale);
		}
	}
	
}
