package io.github.vhoyon.vramework.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.ved.jrequester.Option;
import io.github.ved.jrequester.Request;

public interface LinkableCommand extends Command {
	
	String getCall();
	
	default String getActualCall(){
		return this.getCall();
	}
	
	default List<String> getAliases(){
		return Collections.emptyList();
	}
	
	default List<String> getAllCalls(){
		String call = this.getCall();
		
		if(call == null)
			return Collections.emptyList();
		
		List<String> aliases = this.getAliases();
		
		List<String> allCalls = new ArrayList<>(aliases.size() + 1);
		
		allCalls.add(call);
		allCalls.addAll(aliases);
		
		return allCalls;
	}
	
	default List<String> getCallsExceptActual() throws IllegalStateException{
		String actualCall = this.getActualCall();
		
		if(actualCall == null)
			throw new IllegalStateException(
					"The actual call should not be null. Maybe this method was called in a class not meant to be called?");
		
		return this.getAllCalls().stream().filter(o -> !actualCall.equals(o))
				.collect(Collectors.toList());
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
		
		String commandDescription = this.getCommandDescription();
		Option[] options = this.getOptions();
		
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
						.append(this.formatOption(option.getName()));
				
				for(String variant : option.getVariants()){
					builder.append(optionSeparator).append(
							this.formatOption(variant));
				}
				
				String optionDescription = option.getDescription();
				if(optionDescription != null)
					builder.append(" : ").append(optionDescription);
				
			}
			
		}
		
		List<String> aliases = this.getAliases();
		
		if(aliases.size() == 0){
			if(textWhenNoAliases != null)
				builder.append("\n\n").append(textWhenNoAliases);
		}
		else{
			
			builder.append("\n");
			
			if(textWhenAliasesAvailable != null){
				builder.append("\n").append(textWhenAliasesAvailable);
			}
			
			aliases.forEach(alias -> {
				builder.append("\n\t").append(this.formatCommand(alias));
			});
			
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
