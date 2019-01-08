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
		String[] allCalls = new String[getAliases().length + 1];
		
		allCalls[0] = getCall();
		
		String[] aliases = getAliases();
		
		for(int i = 0; i < aliases.length; i++){
			allCalls[i + 1] = aliases[i];
		}
		
		return allCalls;
	}
	
	default String[] getCallsExceptActual(){
		return Arrays.stream(getAllCalls())
				.filter(o -> !getActualCall().equals(o)).toArray(String[]::new);
	}
	
	default String getCommandDescription(){
		return null;
	}
	
	default ParametersHelp[] getParametersDescriptions(){
		return null;
	}
	
	default String getHelp(String textWhenParametersAvailable,
			String textHeader, String textWhenNoParameters){
		
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
				builder.append("\n\t").append(textWhenNoParameters);
		}
		else{
			
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
		
		return builder.toString();
		
	}
	
	default String formatParameter(String parameterToFormat){
		return Request.DEFAULT_PARAMETER_PREFIX + parameterToFormat;
	}
	
}
