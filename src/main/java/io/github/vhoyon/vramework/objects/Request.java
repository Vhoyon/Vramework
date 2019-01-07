package io.github.vhoyon.vramework.objects;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.modules.Logger;

public class Request implements Utils {
	
	public class Parameter {
		
		public static final int DEFAULT_WEIGHT = 0;
		
		private String parameterName;
		private String parameterContent;
		
		private int position;
		private boolean acceptsContent;
		
		private int weigthPosition;
		private int weight;
		
		protected Parameter(){}
		
		public Parameter(String parameter){
			this(parameter, -1);
		}
		
		protected Parameter(String paramName, int position){
			this(paramName, null, position);
		}
		
		public Parameter(String paramName, String paramContent){
			this(paramName, paramContent, -1);
		}
		
		protected Parameter(String paramName, String paramContent, int position){
			this(paramName, paramContent, position, DEFAULT_WEIGHT);
		}
		
		protected Parameter(String paramName, String paramContent,
				int position, int weight){
			
			if(paramName.matches(getParametersPrefixProtected() + "{1,2}.+")){
				
				int paramDeclaratorLength = paramName
						.matches(getParametersPrefixProtected() + "{2}.+") ? 2
						: 1;
				
				this.parameterName = paramName.substring(paramDeclaratorLength);
				
			}
			else{
				this.parameterName = paramName;
			}
			
			this.setPosition(position);
			this.setContent(paramContent);
			this.acceptsContent = true;
			this.setWeight(weight);
			
		}
		
		public String getName(){
			return parameterName;
		}
		
		public String getContent(){
			if(parameterContent == null)
				return null;
			else
				return parameterContent;
		}
		
		protected void setContent(String parameterContent){
			if(parameterContent == null)
				this.parameterContent = null;
			else
				this.parameterContent = parameterContent.replaceAll("\"", "");
		}
		
		public int getPosition(){
			return this.position;
		}
		
		protected void setPosition(int position){
			this.position = position;
		}
		
		public boolean doesAcceptContent(){
			return this.acceptsContent;
		}
		
		protected void setAcceptingContent(boolean acceptsContent){
			this.acceptsContent = acceptsContent;
			
			if(!acceptsContent && getContent() != null){
				setContent(null);
			}
		}
		
		public int getWeightPosition(){
			return this.weigthPosition;
		}
		
		public int getWeight(){
			return this.weight;
		}
		
		protected int setWeight(int weightPosition){
			
			if(weightPosition == DEFAULT_WEIGHT){
				this.weigthPosition = DEFAULT_WEIGHT;
				this.weight = DEFAULT_WEIGHT;
			}
			else{
				
				int weight = calculateWeight(weightPosition - 1);
				
				if(weight == -1)
					return -1;
				
				this.weigthPosition = weightPosition;
				this.weight = weight;
				
			}
			
			return this.weight;
			
		}
		
		@Override
		public boolean equals(Object obj){
			
			if(obj instanceof Parameter){
				
				if(getName() == null)
					return false;
				
				Parameter parameterToCompare = (Parameter)obj;
				
				return getName().equals(parameterToCompare.getName());
				
			}
			
			return false;
		}
		
		@Override
		public String toString(){
			return this.getContent();
		}
		
	}
	
	public final static String DEFAULT_COMMAND_PREFIX = "!";
	public final static char DEFAULT_PARAMETER_PREFIX = '-';
	
	private String initialMessage;
	
	private String command;
	private String content;
	
	private String commandPrefix;
	
	private HashMap<String, Parameter> parameters;
	private HashMap<Parameter, ArrayList<String>> parametersLinks;
	private char parametersPrefix;
	
	private ArrayList<Parameter> duplicateParams;
	
	public Request(String[] args){
		this(args, DEFAULT_PARAMETER_PREFIX);
	}
	
	public Request(String receivedMessage){
		this(receivedMessage, DEFAULT_PARAMETER_PREFIX);
	}
	
	public Request(String[] args, char parametersPrefix){
		this(args, DEFAULT_COMMAND_PREFIX, parametersPrefix);
	}
	
	public Request(String receivedMessage, char parametersPrefix){
		this(receivedMessage, DEFAULT_COMMAND_PREFIX, parametersPrefix);
	}
	
	public Request(String[] args, String commandPrefix, char parametersPrefix){
		this(buildMessageFromArgs(args, parametersPrefix), commandPrefix,
				parametersPrefix);
	}
	
	public Request(String receivedMessage, String commandPrefix,
			char parametersPrefix){
		
		this.initialMessage = receivedMessage;
		
		this.commandPrefix = commandPrefix;
		
		this.parametersPrefix = parametersPrefix;
		
		if(!receivedMessage.startsWith(commandPrefix)){
			setContent(receivedMessage);
		}
		else{
			
			String[] messageSplit = splitCommandAndContent(receivedMessage);
			
			setCommand(messageSplit[0]);
			setContent(messageSplit[1]);
			
		}
		
		if(hasContent()){
			setupParameters();
		}
		
	}
	
	public static int calculateWeight(int weightPosition){
		
		double weight = Math.pow(2, weightPosition);
		
		if(weight > Integer.MAX_VALUE){
			Logger.log("Weight index is too high to deal with...",
					Logger.LogType.WARNING);
			
			return -1;
		}
		
		return (int)weight;
		
	}
	
	private class PossibleParam {
		String name;
		int index;
		int startPos;
		int endPos;
		
		public PossibleParam(String name, int index, int startPos, int endPos){
			this.name = name;
			this.index = index;
			this.startPos = startPos;
			this.endPos = endPos;
		}
	}
	
	private void setupParameters(){
		
		parameters = new HashMap<>();
		
		// Splits the content : Search for all spaces, except thoses
		// in double quotes and put all what's found in the
		// possibleParams ArrayList.
		// Necessary since .split() removes the wanted Strings.
		List<String> possibleParams = splitSpacesExcludeQuotes(content);
		
		duplicateParams = new ArrayList<>();
		
		boolean canRoll = true;
		
		for(int i = 0; i < possibleParams.size() && canRoll; i++){
			
			PossibleParam possibleParam = new PossibleParam(
					possibleParams.get(i), i, -1, -1);
			
			if(possibleParam.name.equals(getParametersPrefix() + ""
					+ getParametersPrefix())){
				// If string is double parameter prefix, remove it and stop taking params
				
				possibleParam.startPos = getContent().indexOf(
						possibleParam.name);
				possibleParam.endPos = possibleParam.startPos
						+ possibleParam.name.length();
				
				canRoll = false;
				
			}
			else if(stringIsParameter(possibleParam.name)){
				// If string is structured as a parameter, create it.
				
				canRoll = handleParameterCreation(possibleParam, possibleParams);
				
			}
			
			if(possibleParam.startPos != -1){
				String contentToRemove = getContent().substring(
						possibleParam.startPos, possibleParam.endPos);
				
				setContent(getContent().replaceFirst(
						Pattern.quote(contentToRemove), ""));
			}
			
			i = possibleParam.index;
			
		}
		
		if(hasContent())
			setContent(getContent().trim().replaceAll("\"", ""));
		
	}
	
	private boolean tryGetParamContent(Parameter newParameter,
			PossibleParam possibleParam, List<String> possibleParamList){
		
		String possibleParamContent;
		
		try{
			possibleParamContent = possibleParamList
					.get(possibleParam.index + 1);
			
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
		
		// If the following String isn't another param, set
		// said String as the content for the current param.
		if(!stringIsParameter(possibleParamContent)){
			
			newParameter.setContent(possibleParamContent);
			
			possibleParam.index++;
			
			possibleParam.endPos = getContent().indexOf(possibleParamContent)
					+ possibleParamContent.length();
			
		}
		
		return true;
		
	}
	
	private boolean stringIsParameter(String string){
		return string != null
				&& string.matches(getParametersPrefixProtected()
						+ "{1,2}[^\\s]+");
	}
	
	private boolean handleParameterCreation(PossibleParam possibleParam,
			List<String> possibleParamsList){
		
		boolean canRoll = true;
		
		possibleParam.startPos = getContent().indexOf(possibleParam.name);
		possibleParam.endPos = possibleParam.startPos
				+ possibleParam.name.length();
		
		if(possibleParam.name.matches(getParametersPrefixProtected()
				+ "{2}[^\\s]+")){
			// Doubled param prefix means that all letters count as one param
			
			Parameter newParam = new Parameter(possibleParam.name,
					possibleParam.index);
			
			if(parameters.containsValue(newParam)){
				
				duplicateParams.add(newParam);
				
			}
			else{
				
				canRoll = tryGetParamContent(newParam, possibleParam,
						possibleParamsList);
				
				parameters.put(newParam.getName(), newParam);
				
			}
			
		}
		else{
			// Single param prefix means that all letters counts as a different param
			
			String[] singleParams = possibleParam.name.substring(1).split("");
			
			for(int j = 0; j < singleParams.length && canRoll; j++){
				
				Parameter newParam = new Parameter(singleParams[j],
						possibleParam.index + j);
				
				if(parameters.containsValue(newParam)){
					
					duplicateParams.add(newParam);
					
				}
				else{
					
					if(j == singleParams.length - 1){
						
						canRoll = tryGetParamContent(newParam, possibleParam,
								possibleParamsList);
						
					}
					
					parameters.put(newParam.getName(), newParam);
					
				}
				
			}
			
		}
		
		return canRoll;
		
	}
	
	public String getInitialMessage(){
		return this.initialMessage;
	}
	
	public String getCommand(){
		if(!this.hasCommand())
			return null;
		
		return this.getCommandNoFormat().substring(getCommandPrefix().length());
	}
	
	public String getCommandNoFormat(){
		return this.command;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public boolean hasCommand(){
		return this.getCommandNoFormat() != null;
	}
	
	public boolean isCommand(){
		return this.hasCommand()
				&& this.getCommandNoFormat().matches(
						"^" + Pattern.quote(getCommandPrefix()) + ".+$");
	}
	
	public boolean isOnlyCommandPrefix(){
		return this.hasCommand()
				&& this.getCommandNoFormat().equals(getCommandPrefix());
	}
	
	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		
		if(content == null || content.length() == 0)
			this.content = null;
		else
			this.content = content;
		
	}
	
	public boolean hasContent(){
		return this.getContent() != null;
	}
	
	public String getCommandPrefix(){
		return this.commandPrefix;
	}
	
	public HashMap<String, Parameter> getParameters(){
		return this.parameters;
	}
	
	public HashMap<Parameter, ArrayList<String>> getParametersLinks(){
		return this.parametersLinks;
	}
	
	public Parameter getParameterFromPosition(int position){
		
		for(Map.Entry<String, Parameter> parameterEntry : this.parameters
				.entrySet()){
			
			if(parameterEntry.getValue().getWeightPosition() == position)
				return parameterEntry.getValue();
			
		}
		
		return null;
		
	}
	
	public Parameter getParameterFromWeight(int weight){
		
		for(Map.Entry<String, Parameter> parameterEntry : this.parameters
				.entrySet()){
			
			if(parameterEntry.getValue().getWeight() == weight)
				return parameterEntry.getValue();
			
		}
		
		return null;
		
	}
	
	public Parameter getParameter(String... parameterNames){
		
		if(!hasParameters()){
			return null;
		}
		
		if(parameterNames == null || parameterNames.length == 0)
			throw new IllegalArgumentException(
					"The parametersName parameter cannot be null / empty!");
		
		for(String parameterName : parameterNames){
			
			if(getParametersLinks() != null){
				
				for(Map.Entry<Parameter, ArrayList<String>> entry : getParametersLinks()
						.entrySet()){
					
					if(entry.getValue().contains(parameterName))
						return entry.getKey();
					
				}
				
			}
			
			Parameter paramFound = getParameters().get(parameterName);
			
			if(paramFound != null)
				return paramFound;
			
		}
		
		return null;
		
	}
	
	public char getParametersPrefix(){
		return this.parametersPrefix;
	}
	
	private String getParametersPrefixProtected(){
		return Pattern.quote(String.valueOf(getParametersPrefix()));
	}
	
	// public boolean hasParameter(String parameterName){
	// 	if(getParameters() == null)
	// 		return false;
	
	// 	return this.getParameters().containsKey(parameterName);
	// }
	
	public boolean hasParameter(String... parameterNames){
		return getParameter(parameterNames) != null;
	}
	
	public boolean hasParameters(){
		return getParameters() != null;
	}
	
	public void onParameterPresent(String parameterName,
			Consumer<Parameter> onParamPresent){
		onParameterPresent(parameterName, onParamPresent, null);
	}
	
	public void onParameterPresent(String parameterName,
			Consumer<Parameter> onParamPresent, Runnable onParamNotPresent){
		
		Parameter param = null;
		
		param = getParameter(parameterName);
		
		if(param != null){
			onParamPresent.accept(param);
		}
		else if(onParamNotPresent != null){
			onParamNotPresent.run();
		}
		
	}
	
	// public boolean addParameter(String paramName){
	// 	return this.getParameters().put(paramName, new Parameter(paramName)) == null;
	// }
	
	// public boolean addParameter(String paramName, String paramContent){
	// 	return this.getParameters().put(paramName,
	// 			new Parameter(paramName, paramContent)) == null;
	// }
	
	private String[] splitCommandAndContent(String command){
		
		// Remove leading / trailing spaces, as well as shrinking consecutives
		// whitespace.
		// Also, this adds a space at the end to force the split to be at least
		// of size 2, which means there will always be a command and some
		// content.
		String[] splitted = command.trim().replaceAll("\\s+", " ").concat(" ")
				.split(" ", 2);
		
		splitted[0] = splitted[0].toLowerCase();
		
		if(splitted[1].length() != 0){
			splitted[1] = splitted[1].trim();
		}
		
		return splitted;
		
	}
	
	public ArrayList<Parameter> getDuplicateParamList(){
		return this.duplicateParams;
	}
	
	public boolean hasError(){
		return this.getDuplicateParamList() != null
				&& this.getDuplicateParamList().size() != 0;
	}
	
	public String getDefaultErrorMessage(){
		
		if(!hasError()){
			return "This request has no errors!";
		}
		else{
			
			String pluralTester;
			
			if(this.getDuplicateParamList().size() == 1)
				pluralTester = "That parameter";
			else
				pluralTester = "Those parameters";
			
			StringBuilder message = new StringBuilder();
			
			message.append(pluralTester).append(
					" has been entered more than once : ");
			
			ArrayList<Parameter> handledDupes = new ArrayList<>();
			
			for(int i = 0; i < this.getDuplicateParamList().size(); i++){
				
				Parameter param = this.getDuplicateParamList().get(i);
				
				if(!handledDupes.contains(param)){
					
					handledDupes.add(param);
					
					message.append("\n");
					
					if(this.getDuplicateParamList().size() != 1)
						message.append(i + 1).append(". ");
					else
						message.append("~ ");
					
					message.append(param.getName());
					
				}
				
			}
			
			if(this.getDuplicateParamList().size() == 1)
				pluralTester = "that parameter";
			else
				pluralTester = "those parameters";
			
			message.append("\n")
					.append(format(
							"Only the first instance of {1} will be taken into consideration.",
							pluralTester));
			
			return message.toString();
			
		}
		
	}
	
	public void setParamLinkMap(ArrayList<ArrayList<String>> map){
		
		if(getParameters() != null)
			getParameters().forEach((key, param) -> {
				
				for(ArrayList<String> paramsGroup : map){
					
					if(paramsGroup.contains(key)){
						
						if(this.parametersLinks == null){
							this.parametersLinks = new HashMap<>();
						}
						
						this.parametersLinks.put(param, paramsGroup);
						break;
						
					}
					
				}
				
			});
		
	}
	
	public void setParamLink(String param, String... links){
		
		if(links.length != 0){
			
			onParameterPresent(param, (parameter) -> {
				
				if(this.parametersLinks == null){
					this.parametersLinks = new HashMap<>();
				}
				
				ArrayList<String> list = new ArrayList<>(Arrays.asList(links));
				
				this.parametersLinks.put(parameter, list);
				
			});
			
		}
		
	}
	
	public void setParameterContentLess(String paramName){
		Parameter paramFound = getParameter(paramName);
		
		if(paramFound == null){
			throw new NullPointerException("Parameter \"" + paramName
					+ "\" is not present or linked in this request.");
		}
		else{
			
			if(paramFound.getContent() != null){
				
				if(!hasContent())
					setContent(paramFound.getContent());
				else
					setContent(getContent() + " " + paramFound.getContent());
				
			}
			
			paramFound.setAcceptingContent(false);
			
		}
	}
	
	public void setParamsAsContentLess(
			ArrayList<String> paramsToTreatAsContentLess){
		this.setParamsAsContentLess(paramsToTreatAsContentLess
				.toArray(new String[0]));
	}
	
	public void setParamsAsContentLess(String[] paramsToTreatAsContentLess){
		
		for(String paramName : paramsToTreatAsContentLess){
			try{
				this.setParameterContentLess(paramName);
			}
			catch(NullPointerException e){}
		}
		
	}
	
	public void setParameterWeight(String parameterName, int weightPosition)
			throws IllegalArgumentException{
		this.setParameterWeight(getParameter(parameterName), weightPosition);
	}
	
	public void setParameterWeight(Parameter parameter, int weightPosition)
			throws IllegalArgumentException{
		
		if(parameter == null){
			throw new IllegalArgumentException("The parameter cannot be null.");
		}
		if(weightPosition < 1){
			throw new IllegalArgumentException(
					"The importance parameter can only be 1 or above.");
		}
		
		final int prevWeightPosition = parameter.getWeightPosition();
		
		if(parameter.setWeight(weightPosition) > Parameter.DEFAULT_WEIGHT){
			
			final boolean isWeightSmaller = prevWeightPosition != Parameter.DEFAULT_WEIGHT
					&& prevWeightPosition > weightPosition;
			
			int smallestPosition = isWeightSmaller ? weightPosition
					: prevWeightPosition;
			
			int vector = isWeightSmaller ? 1 : -1;
			
			this.parameters.forEach((s, param) -> {
				
				if(param.getWeight() != Parameter.DEFAULT_WEIGHT
						&& prevWeightPosition != 0
						&& !parameter.equals(param)
						&& param.getWeightPosition() >= smallestPosition
						&& param.getWeightPosition() <= parameter
								.getWeightPosition()){
					
					param.setWeight(param.getWeightPosition() + vector);
					
				}
				
			});
			
		}
		
	}
	
	public static String buildMessageFromArgs(String[] args,
			char parametersPrefix){
		StringBuilder builder = new StringBuilder();
		
		for(String arg : args){
			
			String protectedParamPrefix = Pattern.quote(String
					.valueOf(parametersPrefix));
			String paramRegex = "^(" + protectedParamPrefix + "{1,2}.+|"
					+ protectedParamPrefix + "{2})$";
			
			if(arg.matches(paramRegex)){
				builder.append(arg);
			}
			else{
				builder.append("\"").append(arg).append("\"");
			}
			
			builder.append(" ");
		}
		
		String argsRequest = builder.toString();
		
		return argsRequest.isEmpty() ? "" : argsRequest.substring(0,
				argsRequest.length() - 1);
	}
	
}
