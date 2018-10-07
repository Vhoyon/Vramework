package io.github.vhoyon.vramework.objects;

import java.util.ArrayList;

public class ParametersHelp {
	
	private String parameterDescription;
	private boolean acceptsContent;
	private String param;
	private String[] paramVariants;
	
	public ParametersHelp(String parameterDescription, String param,
			String... paramVariants){
		this(parameterDescription, true, param, paramVariants);
	}
	
	public ParametersHelp(String parameterDescription, boolean acceptsContent, String param,
			String... paramVariants){
		
		this.parameterDescription = parameterDescription;
		this.acceptsContent = acceptsContent;
		this.param = param;
		this.paramVariants = paramVariants;
		
	}
	
	public String getParameterDescription(){
		return this.parameterDescription;
	}
	
	public boolean doesAcceptsContent(){
		return this.acceptsContent;
	}
	
	public String getParam(){
		return this.param;
	}
	
	public String[] getParamVariants(){
		return this.paramVariants;
	}
	
	public boolean hasParam(String param){
		return getAllParams().contains(param);
	}
	
	public ArrayList<String> getAllParams(){
		ArrayList<String> allParams = new ArrayList<>();
		
		allParams.add(getParam());
		
		for(String paramVariant : getParamVariants())
			allParams.add(paramVariant);
		
		return allParams;
	}
	
}
