package io.github.vhoyon.vramework.interfaces;

import java.util.Arrays;

import io.github.vhoyon.vramework.objects.ParametersHelp;
import io.github.vhoyon.vramework.objects.Request;

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
	
	default ParametersHelp[] getParametersDescriptions(){
		return null;
	}
	
	default String getHelp(String textHeader,
			String textWhenParametersAvailable, String textWhenNoParameters){
		return this.getHelp(textHeader, textWhenParametersAvailable,
				textWhenNoParameters, "Aliases :");
	}
	
	default String getHelp(String textHeader,
			String textWhenParametersAvailable, String textWhenNoParameters,
			String textWhenAliasesAvailable){
		return this.getHelp(textHeader, textWhenParametersAvailable,
				textWhenNoParameters, textWhenAliasesAvailable, null);
	}
	
	default String getHelp(String textHeader,
			String textWhenParametersAvailable, String textWhenNoParameters,
			String textWhenAliasesAvailable, String textWhenNoAliases){
		
		String commandDescription = getCommandDescription();
		ParametersHelp[] parametersHelp = getParametersDescriptions();
		
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
		
		if(parametersHelp == null){
			if(textWhenNoParameters != null)
				builder.append("\n\n").append(textWhenNoParameters);
		}
		else{
			
			builder.append("\n");
			
			String paramsSeparator = ", ";
			
			if(textWhenParametersAvailable != null)
				builder.append("\n").append(textWhenParametersAvailable);
			
			for(ParametersHelp paramHelp : parametersHelp){
				
				builder.append("\n").append("\t")
						.append(formatParameter(paramHelp.getParam()));
				
				for(String param : paramHelp.getParamVariants()){
					builder.append(paramsSeparator).append(
							formatParameter(param));
				}
				
				String paramHelpDescription = paramHelp
						.getParameterDescription();
				if(paramHelpDescription != null)
					builder.append(" : ").append(paramHelpDescription);
				
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
	
	default String formatParameter(String parameterToFormat){
		return Request.DEFAULT_PARAMETER_PREFIX + parameterToFormat;
	}
	
}
