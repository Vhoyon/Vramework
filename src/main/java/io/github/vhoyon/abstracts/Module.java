package io.github.vhoyon.abstracts;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Module {
	
	private static boolean hasWarnings;
	private static boolean hasErrors;
	
	private static HashMap<String, ArrayList<String>> warnings;
	private static HashMap<String, ArrayList<String>> errors;
	
	public abstract void build() throws Exception;
	
	public String getLoadingErrorMessage(Exception e){
		String moduleFailMessage = "The module " + this.getClass()
				+ " couldn't load.";
		
		return moduleFailMessage + "\n\n" + e.getMessage();
	}
	
	public void addWarning(String key, String warning){
		if(!hasWarnings){
			hasWarnings = true;
			warnings = new HashMap<>();
		}
		
		if(!warnings.containsKey(key)){
			warnings.put(key, new ArrayList<>());
		}
		
		warnings.get(key).add(warning);
	}
	
	public void addError(String key, String error){
		if(!hasErrors){
			hasErrors = true;
			errors = new HashMap<>();
		}
		
		if(!errors.containsKey(key)){
			errors.put(key, new ArrayList<>());
		}
		
		errors.get(key).add(error);
	}
	
	public boolean hasWarnings(){
		return hasWarnings;
	}
	
	public boolean hasErrors(){
		return hasErrors;
	}
	
	public ArrayList<String> getWarnings(String key){
		return warnings.get(key);
	}
	
	public ArrayList<String> getErrors(String key){
		return errors.get(key);
	}
	
	public boolean hasIssues(){
		return hasWarnings() || hasErrors();
	}
	
	public void handleIssues() throws Exception{
		if(hasIssues()){
			handleIssuesLogic();
		}
	}
	
	/**
	 * Non obligatory method to handle the logic of possible issues, called in
	 * the {@link #handleIssues()} method if there is issues.
	 */
	protected void handleIssuesLogic() throws Exception{}
	
}
