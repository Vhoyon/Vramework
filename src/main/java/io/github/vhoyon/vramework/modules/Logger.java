package io.github.vhoyon.vramework.modules;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.vhoyon.vramework.abstracts.ModuleOutputtable;
import io.github.vhoyon.vramework.interfaces.Loggable;

public class Logger extends ModuleOutputtable {
	
	/**
	 * The type of logging to prepend to the log message.
	 * <p>
	 * Take note that will be added to the message is literally the enum's
	 * value.
	 */
	public enum LogType{
		INFO, WARNING, ERROR
	}
	
	private static List<Loggable> outputs;
	
	private static boolean hasIssuedWarning;
	
	private static String separator;
	
	@Override
	public void build(){
		outputs = new ArrayList<>();
		hasIssuedWarning = false;
		separator = "-";
	}
	
	protected static List<Loggable> getOutputs(){
		return outputs;
	}
	
	public static boolean hasOutputs(){
		List<Loggable> outputs = getOutputs();
		return outputs != null && !outputs.isEmpty();
	}
	
	/**
	 * Sets the separator for logs that has a prefix before the message.
	 * <p>
	 * Set to {@code null} if you don't want any separator.
	 */
	public static void setSeparator(String newSeparator){
		separator = newSeparator;
	}
	
	/**
	 * Overwrite the output list with those passed as parameters.
	 * 
	 * @param outputs
	 *            Objects implementing the
	 *            {@link io.github.vhoyon.vramework.interfaces.Loggable
	 *            Loggable} interface which are meant to receive logs from the
	 *            Logger module.
	 */
	public static void setOutputs(Loggable... outputs){
		if(outputs != null)
			Logger.outputs = new ArrayList<>(Arrays.asList(outputs));
	}
	
	/**
	 * Adds an output to the already existing output list for the Logger module.
	 * 
	 * @param output
	 *            Object implementing the
	 *            {@link io.github.vhoyon.vramework.interfaces.Loggable
	 *            Loggable} interface which is meant to receive logs from the
	 *            Logger module.
	 * @return {@code true} if the output was added successfully, {@code false}
	 *         if the output is already in the outputs list.
	 */
	public static boolean addOutput(Loggable output){
		
		List<Loggable> outputs = getOutputs();
		
		if(outputs.contains(output)){
			return false;
		}
		
		return outputs.add(output);
		
	}
	
	/**
	 * Adds multiple outputs to the already existing output list for the Logger
	 * module.
	 * 
	 * @param outputs
	 *            Objects implementing the
	 *            {@link io.github.vhoyon.vramework.interfaces.Loggable
	 *            Loggable} interface which are meant to receive logs from the
	 *            Logger module.
	 */
	public static void addOutputs(Loggable... outputs){
		for(Loggable output : outputs){
			Logger.addOutput(output);
		}
	}
	
	/**
	 * Removes an output from the output list for the Logger module.
	 * 
	 * @param output
	 *            Object implementing the
	 *            {@link io.github.vhoyon.vramework.interfaces.Loggable
	 *            Loggable} interface.
	 * @return {@code true} if the output was in the list and has been
	 *         removed, {@code false} if the output is not in the list.
	 */
	public static boolean removeOutput(Loggable output){
		return getOutputs().remove(output);
	}
	
	/**
	 * Removes an output from the output list by it's index for the Logger
	 * module.
	 * 
	 * @param index
	 *            The position of the output in the internal list.
	 * @return The {@link io.github.vhoyon.vramework.interfaces.Loggable
	 *         Loggable} object found at
	 *         this position, or {@code null} if the index is out of
	 *         bounds.
	 */
	public static Loggable removeOutput(int index){
		try{
			return getOutputs().remove(index);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public static void log(String message){
		log(message, (String)null, true);
	}
	
	public static void log(String message, LogType logType){
		log(message, logType, true);
	}
	
	public static void log(String message, boolean appendDate){
		log(message, (String)null, appendDate);
	}
	
	public static void log(String message, LogType logType, boolean appendDate){
		log(message, logType.toString(), appendDate);
	}
	
	public static void log(Exception e){
		log(e.getMessage(), LogType.ERROR.toString(), true, e.getStackTrace());
	}
	
	public static void log(String message, final String logType,
			final boolean appendDate){
		log(message, logType, appendDate, Thread.currentThread()
				.getStackTrace());
	}
	
	private static void log(String message, String logType,
			final boolean appendDate, StackTraceElement[] stackElements){
		
		if(message == null || message.length() == 0){
			log("[tried to log empty message]", LogType.ERROR, true);
		}
		else{
			
			final ArrayList<Loggable> outputs = getOutputs();
			
			final String logText = buildLogMessage(message, logType,
					appendDate, stackElements);
			
			hasIssuedWarning = handleMessageAndWarning(logText, outputs,
					hasIssuedWarning,
					(output) -> output.log(logText, logType, appendDate));
			
		}
		
	}
	
	private static String buildLogMessage(String message, final String logType,
			final boolean appendDate, final StackTraceElement[] stackElements){
		
		StringBuilder builder = new StringBuilder();
		
		Matcher matcher = Pattern.compile("^\\^?(\\s+\\^)?\\s+").matcher(
				message);
		
		if(matcher.find()){
			
			int whitespaceStartIndex = matcher.start();
			int whitespaceEndIndex = matcher.end();
			
			String beforehandWhitespace = message.substring(
					whitespaceStartIndex, whitespaceEndIndex);
			
			if(!message.startsWith("^")){
				message = message.substring(whitespaceEndIndex);
				
				builder.append(beforehandWhitespace);
			}
			else{
				message = message.substring(1);
				
				int secondCaretPos = message.indexOf("^");
				
				// No second caret
				if(secondCaretPos != -1){
					
					message = message.substring(secondCaretPos + 1);
					
					builder.append(beforehandWhitespace, 1, secondCaretPos + 1);
					
				}
				
			}
			
		}
		
		boolean hasAddedPrefix = false;
		
		if(appendDate){
			String dateTime = LocalDateTime.now().format(
					DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			
			builder.append("[").append(dateTime).append("]");
			
			hasAddedPrefix = true;
		}
		
		if(logType != null){
			builder.append("[").append(logType).append("]");
			
			hasAddedPrefix = true;
		}
		
		if(hasAddedPrefix){
			
			if(separator != null){
				builder.append(" ").append(separator);
			}
			
			builder.append(" ");
			
		}
		
		builder.append(message);
		
		if(LogType.ERROR.toString().equalsIgnoreCase(logType)){
			
			String className = null;
			String methodName = null;
			int lineNumber = -1;
			
			for(StackTraceElement stackElement : stackElements){
				
				if(!stackElement.equals(stackElements[0])
						&& !stackElement.getClassName().equals(
								Logger.class.getCanonicalName())){
					
					className = stackElement.getClassName();
					methodName = stackElement.getMethodName();
					lineNumber = stackElement.getLineNumber();
					
					break;
					
				}
				
			}
			
			String errorMessage = String.format(
					"Error in %1$s at line %2$s (in method `%3$s`).",
					className, lineNumber, methodName);
			
			builder.append("\n\t").append(errorMessage);
			
		}
		
		return builder.toString();
		
	}
	
}
