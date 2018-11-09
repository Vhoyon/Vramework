package io.github.vhoyon.vramework.modules;

import io.github.vhoyon.vramework.Framework;
import io.github.vhoyon.vramework.abstracts.AbstractTerminalConsole;
import io.github.vhoyon.vramework.abstracts.Module;
import io.github.vhoyon.vramework.exceptions.BadFileContentException;
import io.github.vhoyon.vramework.interfaces.Console;
import io.github.vhoyon.vramework.ui.NotificationUI;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment extends Module {
	
	private static Environment singleton;
	
	private static final String ENV_FILE_NAME = ".env";
	private static final String ENV_EXAMPLE_FILE_NAME = "example.env";
	
	private static HashMap<String, String> envVars;
	
	private static final String WARNINGS = "WARNINGS";
	private static final String SPACED_KEY_ERRORS = "SPACED_KEY_ERRORS";
	private static final String LINE_ERRORS = "LINE_ERRORS";
	
	public void build() throws Exception{
		singleton = new Environment();
		refresh();
	}
	
	public static void refresh() throws FileNotFoundException, RuntimeException{
		refresh(null);
	}
	
	public static void refresh(String folderPath) throws FileNotFoundException,
			RuntimeException{
		
		if(singleton == null)
			return;
		
		try{
			
			BufferedReader reader = getEnvFile(folderPath);
			
			envVars = new HashMap<>();
			
			String line;
			
			for(int i = 1; (line = reader.readLine()) != null; i++){
				
				// Remove comment from line if there's content in the line
				if(line.length() != 0){
					line = removeCommentFromLine(line);
				}
				
				if(line.length() != 0){
					
					String[] keyValue = line.trim().split("=", 2);
					
					if(keyValue.length != 2){
						
						Environment.singleton.addError(LINE_ERRORS, "Line " + i
								+ ": \"" + line + "\"");
						
					}
					else{
						
						if(keyValue[1].length() == 0){
							Environment.singleton
									.addWarning(
											WARNINGS,
											"The variable keyed \""
													+ keyValue[0]
													+ "\" at line "
													+ i
													+ " is empty, is that supposed to be..?");
						}
						
						if(keyValue[0].contains(" ")){
							
							Environment.singleton.addError(SPACED_KEY_ERRORS,
									"Line " + i + ": \"" + line + "\"");
							
						}
						
					}
					
					if(!Environment.singleton.hasErrors()){
						envVars.put(keyValue[0].toLowerCase(), keyValue[1]);
					}
					
				}
				
			}
			
			reader.close();
			
			try{
				Environment.singleton.handleIssues();
			}
			catch(Exception e){
				throw new RuntimeException(e);
			}
			
		}
		catch(IOException e){
			throw new FileNotFoundException(
					"An unexpected problem happened while reading the line of the file.");
		}
		
	}
	
	private static String removeCommentFromLine(String line){
		Matcher matcher = Pattern.compile("(\\s)*#.*").matcher(line);
		int index = matcher.find() ? matcher.start() : -1;
		
		if(index != -1)
			return line.substring(0, index);
		
		return line;
	}
	
	public static <EnvVar> EnvVar getVar(String key){
		return getVar(key, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <EnvVar> EnvVar getVar(String key, EnvVar defaultValue){
		if(envVars == null){
			
			if(Framework.isDebugging())
				Logger.log(
						"A call to get a variable environment has been used but the Environment is not yet set! Make sure you have built the Framework or call Environment.build() manually! Using the defaultObject provided in the meantime.",
						Logger.LogType.WARNING);
			
			return defaultValue;
			
		}
		
		String value = envVars.get(key.toLowerCase());
		
		if(value == null || value.equals("")){
			return defaultValue;
		}
		
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
			return (EnvVar)Boolean.valueOf(value);
		}
		else if(value.matches("^[0-9]+$")){
			
			try{
				Integer number = Integer.valueOf(value);
				
				return (EnvVar)number;
			}
			catch(NumberFormatException e){}
			
		}
		else if(value.matches("^[0-9]+(\\.[0-9]+)?$")){
			
			try{
				Double number = Double.valueOf(value);
				
				return (EnvVar)number;
			}
			catch(NumberFormatException e){}
			
		}
		
		return (EnvVar)value;
	}
	
	public static boolean hasVar(String key){
		return envVars.containsKey(key.toLowerCase());
	}
	
	@Override
	protected void handleIssuesLogic() throws Exception{
		
		StringBuilder builder = new StringBuilder();
		
		if(!hasErrors()){
			
			builder.append("WARNINGS!");
			
			ArrayList<String> warnings = getWarnings(WARNINGS);
			
			if(warnings != null){
				
				builder.append("\n");
				
				for(String warning : warnings){
					builder.append("\n").append(warning);
				}
				
			}
			
			System.err.println(builder.append("\n").toString());
			
		}
		else{
			
			builder.append("ERRORS!");
			
			ArrayList<String> spaceErrors = getErrors(SPACED_KEY_ERRORS);
			
			if(spaceErrors != null){
				
				builder.append("\n\nSome keys ("
						+ spaceErrors.size()
						+ ") has space(s) in them : Keys must NOT contain spaces.\n");
				
				for(String error : spaceErrors){
					builder.append("\n").append(error);
				}
				
			}
			
			ArrayList<String> lineErrors = getErrors(LINE_ERRORS);
			
			if(lineErrors != null){
				
				builder.append("\n\nSome variables ("
						+ lineErrors.size()
						+ ") are not well formatted. Please follow the \"KEY=value\" format.\n");
				
				for(String error : lineErrors){
					builder.append("\n").append(error);
				}
				
			}
			
			throw new BadFileContentException(builder.toString());
			
		}
		
	}
	
	private static BufferedReader getEnvFile(String folderPath){
		
		InputStream inputStream;
		
		String systemEnvFilePath = buildEnvFilePath();
		
		try{
			
			inputStream = new FileInputStream(new File(systemEnvFilePath));
			
		}
		catch(FileNotFoundException e){
			
			inputStream = Framework.class.getResourceAsStream("/"
					+ ENV_FILE_NAME);
			
			if(inputStream == null){
				
				Console console;
				
				if(Framework.isRunningFromTerminal()){
					
					console = new AbstractTerminalConsole(){
						@Override
						public void initialize(boolean startImmediately){
							Logger.setOutputs(this);
						}
					};
					
				}
				else{
					console = new NotificationUI();
				}
				
				try{
					console.initialize();
					
					boolean shouldExit = confirmEnvFileCreation(console,
							folderPath);
					
					if(shouldExit)
						System.exit(0);
				}
				catch(Exception nothing){}
				
				int shouldExitChoice = console
						.getConfirmation(
								"Enter yes to continue startup after updating your env file. If you want to exit, enter no",
								Console.QuestionType.YES_NO);
				
				if(shouldExitChoice == Console.NO){
					System.exit(0);
				}
				else{
					
					try{
						inputStream = new FileInputStream(new File(
								systemEnvFilePath));
					}
					catch(FileNotFoundException e1){
						console.getConfirmation(
								"Failed getting environment file. Exiting.",
								Console.QuestionType.OK);
						
						System.exit(1);
					}
					
				}
				
			}
			
		}
		
		InputStreamReader streamReader = new InputStreamReader(inputStream,
				StandardCharsets.UTF_8);
		
		return new BufferedReader(streamReader);
		
	}
	
	private static boolean confirmEnvFileCreation(Console console,
			String folderPath){
		
		int choice = console
				.getConfirmation(
						"No environment file has been detected, do you want to create one now?",
						Console.QuestionType.YES_NO);
		
		switch(choice){
		case Console.YES:
			
			try{
				
				String envFilePath = buildSystemEnvFile(folderPath);
				
				int openFileNow = console.getConfirmation(
						"Open file now in default editor?",
						Console.QuestionType.YES_NO);
				
				if(openFileNow == Console.YES){
					
					Desktop.getDesktop().edit(new File(envFilePath));
					
				}
				else{
					
					Logger.log(
							"Please go fill the environment file with your own informations and start this program again!"
									+ "\n"
									+ "Path of the file created : \""
									+ envFilePath + "\"", Logger.LogType.INFO,
							false);
					
					return true;
					
				}
				
			}
			catch(IOException e){
				Logger.log(e);
			}
			
			break;
		case Console.NO:
			Logger.log("No environment added, bot stopping.",
					Logger.LogType.INFO, false);
			
			return true;
		}
		
		return false;
		
	}
	
	private static String buildSystemEnvFile(String folderPath)
			throws IOException{
		
		InputStream exampleFileStream = Framework.class.getResourceAsStream("/"
				+ ENV_EXAMPLE_FILE_NAME);
		
		byte[] buffer = new byte[exampleFileStream.available()];
		exampleFileStream.read(buffer);
		
		String systemEnvFilePath = buildEnvFilePath(folderPath);
		
		File targetFile = new File(systemEnvFilePath);
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		
		outStream.close();
		
		return systemEnvFilePath;
		
	}
	
	private static String buildEnvFilePath(String folderPath){
		if(folderPath == null)
			return buildEnvFilePath();
		
		return folderPath
				+ (folderPath.endsWith(File.separator) ? "" : File.separator)
				+ ENV_FILE_NAME;
	}
	
	private static String buildEnvFilePath(){
		return buildEnvFilePath(Framework.runnableSystemPath());
	}
	
}
