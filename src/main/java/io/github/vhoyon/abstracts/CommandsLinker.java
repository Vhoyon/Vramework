package io.github.vhoyon.abstracts;

import io.github.vhoyon.objects.CommandLinksContainer;
import io.github.vhoyon.objects.Link;

import java.util.HashMap;
import java.util.TreeMap;

public abstract class CommandsLinker extends Translatable {
	
	private CommandLinksContainer container;
	
	public abstract CommandLinksContainer createLinksContainer();
	
	public CommandLinksContainer getContainer(){
		if(container != null)
			return container;
		
		return container = createLinksContainer();
	}
	
	public String getFullHelpString(String textHeader){
		return getFullHelpString(textHeader, false);
	}
	
	public String getFullHelpString(String textHeader, boolean showDescriptions){
		return getFullHelpString(textHeader, showDescriptions, true);
	}
	
	public String getFullHelpString(String textHeader,
			boolean showDescriptions, boolean shouldSummarize){
		
		CommandLinksContainer container = getContainer();
		
		StringBuilder builder = new StringBuilder();
		
		if(textHeader != null){
			builder.append(textHeader).append("\n\n");
		}
		
		HashMap<String, Link> linksMap = container.getLinkMap();
		
		TreeMap<String, Link> defaultCommands = new TreeMap<>();
		
		linksMap.forEach((key, link) -> {
			
			boolean isSubstitute = defaultCommands.containsKey(link
					.getDefaultCall());
			
			if(!isSubstitute){
				defaultCommands.put(link.getDefaultCall(), link);
			}
			
		});
		
		String prependChars = getPrependChars();
		String prependCharsVariants = getPrependCharsForVariants();
		
		defaultCommands.forEach((key, link) -> {
			
			String wholeCommandString = formatWholeCommand(prependChars, key);
			
			if(!showDescriptions){
				builder.append(wholeCommandString);
			}
			else{
				
				String wholeHelpString = null;
				
				// Try to get the help string of a link
				try{
					
					String helpString = link.getInstance()
							.getCommandDescription();
					
					wholeHelpString = formatHelpString(helpString);
					
				}
				catch(Exception e){}
				
				if(wholeHelpString == null){
					builder.append(wholeCommandString);
				}
				else{
					builder.append(formatWholeHelpLine(wholeCommandString,
							wholeHelpString, shouldSummarize));
				}
				
			}
			
			builder.append("\n");
			
			Object calls = link.getCalls();
			
			if(calls instanceof String[]){
				
				String[] callsArray = (String[])calls;
				
				// Add all of the non default calls of a link as a variant
				for(int i = 1; i < callsArray.length; i++){
					
					builder.append(formatVariant(formatWholeCommand(
							prependCharsVariants, callsArray[i])));
					
					builder.append("\n");
					
				}
				
			}
			
		});
		
		return builder.toString().trim();
		
	}
	
	private String formatWholeCommand(String prependChars, String command){
		if(prependChars == null || prependChars.length() == 0)
			return formatCommand(command);
		
		return prependChars + formatCommand(command);
	}
	
	public String formatWholeHelpLine(String wholeCommandString,
			String wholeHelpString, boolean shouldSummarize){
		
		String helpString;
		int returnIndex;
		
		if(shouldSummarize
				&& (returnIndex = wholeHelpString.indexOf("\n")) != -1){
			helpString = wholeHelpString.substring(0, returnIndex);
		}
		else{
			helpString = wholeHelpString;
		}
		
		return wholeCommandString + " : " + helpString;
		
	}
	
	public String formatCommand(String command){
		return command;
	}
	
	public String formatHelpString(String helpString){
		return helpString;
	}
	
	public String formatVariant(String variant){
		return "\t" + variant;
	}
	
	public String getPrependChars(){
		return "~ ";
	}
	
	public String getPrependCharsForVariants(){
		return getPrependChars();
	}
	
}
