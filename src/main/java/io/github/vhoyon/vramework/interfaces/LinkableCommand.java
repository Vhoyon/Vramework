package io.github.vhoyon.vramework.interfaces;

import java.util.Arrays;

import io.github.ved.jrequester.Option;
import io.github.ved.jrequester.Request;

public interface LinkableCommand extends Command {
	
	String getCall();
	
	default String getActualCall(){
		return this.getCall();
	}
	
	default String[] getAliases(){
		return new String[0];
	}
	
	default String[] getAllCalls(){
		String call = getCall();
		
		if(call == null)
			return new String[0];
		
		String[] aliases = getAliases();
		
		String[] allCalls = new String[aliases.length + 1];
		
		allCalls[0] = call;
		
		for(int i = 0; i < aliases.length; i++){
			allCalls[i + 1] = aliases[i];
		}
		
		return allCalls;
	}
	
	default String[] getCallsExceptActual() throws IllegalStateException{
		String actualCall = getActualCall();
		
		if(actualCall == null)
			throw new IllegalStateException(
					"The actual call should not be null. Maybe this method was called in a class not meant to be called?");
		
		return Arrays.stream(getAllCalls()).filter(o -> !actualCall.equals(o))
				.toArray(String[]::new);
	}
	
	default String getCommandDescription(){
		return null;
	}
	
	default Option[] getOptions(){
		return null;
	}
	
	default String getHelp(String textHeader, String textWhenOptionsAvailable,
			String textWhenNoOptions){
		return this.getHelp(textHeader, textWhenOptionsAvailable,
				textWhenNoOptions, "Aliases :");
	}
	
	default String getHelp(String textHeader, String textWhenOptionsAvailable,
			String textWhenNoOptions, String textWhenAliasesAvailable){
		return this.getHelp(textHeader, textWhenOptionsAvailable,
				textWhenNoOptions, textWhenAliasesAvailable, null);
	}
	
	default String getHelp(String textHeader, String textWhenOptionsAvailable,
			String textWhenNoOptions, String textWhenAliasesAvailable,
			String textWhenNoAliases){
		
		String commandDescription = getCommandDescription();
		Option[] options = getOptions();
		
		StringBuilder builder = new StringBuilder();
		
		if(textHeader != null)
			builder.append(textHeader).append("\n");
		
		if(commandDescription != null){
			
			if(textHeader != null){
				builder.append("\t");
				
				commandDescription = commandDescription
						.replaceAll("\n", "\n\t");
			}
			
			builder.append(commandDescription);
			
		}
		
		if(options == null){
			if(textWhenNoOptions != null)
				builder.append("\n\n").append(textWhenNoOptions);
		}
		else{
			
			builder.append("\n");
			
			String optionSeparator = ", ";
			
			if(textWhenOptionsAvailable != null)
				builder.append("\n").append(textWhenOptionsAvailable);
			
			for(Option option : options){
				
				builder.append("\n").append("\t")
						.append(formatOption(option.getName()));
				
				for(String variant : option.getVariants()){
					builder.append(optionSeparator).append(
							formatOption(variant));
				}
				
				String optionDescription = option.getDescription();
				if(optionDescription != null)
					builder.append(" : ").append(optionDescription);
				
			}
			
		}
		
		String[] aliases = this.getAliases();
		
		if(aliases.length == 0){
			if(textWhenNoAliases != null)
				builder.append("\n\n").append(textWhenNoAliases);
		}
		else{
			
			builder.append("\n");
			
			if(textWhenAliasesAvailable != null)
				builder.append("\n").append(textWhenAliasesAvailable);
			
			for(String alias : aliases){
				builder.append("\n").append("\t").append(formatCommand(alias));
			}
			
		}
		
		return builder.toString();
		
	}
	
	default String formatCommand(String commandToFormat){
		return Request.DEFAULT_COMMAND_PREFIX + commandToFormat;
	}
	
	default String formatOption(String optionToFormat){
		return Request.DEFAULT_OPTION_PREFIX + optionToFormat;
	}
	
}
