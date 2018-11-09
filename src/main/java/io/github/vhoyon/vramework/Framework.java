package io.github.vhoyon.vramework;

import io.github.vhoyon.vramework.abstracts.Module;

import java.awt.*;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;

public class Framework {
	
	private static Class<?> classRunIn;
	
	private static boolean IS_RUNNING_FROM_TERMINAL;
	private static String RUNNABLE_SYSTEM_PATH;
	private static Date BUILD_STARTED_AT;
	private static boolean IS_DEBUGGING;
	
	private Framework(){}
	
	public static boolean isRunningFromTerminal(){
		return IS_RUNNING_FROM_TERMINAL;
	}
	
	public static String runnableSystemPath(){
		return RUNNABLE_SYSTEM_PATH;
	}
	
	public static Date buildStartedAt(){
		return BUILD_STARTED_AT;
	}
	
	public static boolean isDebugging(){
		return IS_DEBUGGING;
	}
	
	private static String[] modules =
	{
		"Environment", "Logger", "Metrics", "Audit"
	};
	
	public static void build(Class<?> caller) throws Exception{
		Framework.build(caller, false);
	}
	
	public static void build(Class<?> caller, boolean isDebugging)
			throws Exception{
		
		if(caller == null)
			throw new IllegalArgumentException("The caller cannot be null!");
		
		Framework.classRunIn = caller;
		
		Framework.setupGlobalVariables(isDebugging);
		
		StringBuilder errors = new StringBuilder();
		
		for(String moduleName : modules){
			
			try{
				String formattedModuleName = moduleName.replaceAll("/", ".");
				
				Class<?> moduleClass = Class
						.forName("io.github.vhoyon.vramework.modules."
								+ formattedModuleName);
				
				if(Module.class.isAssignableFrom(moduleClass)){
					
					Module module = (Module)moduleClass.newInstance();
					
					try{
						module.build();
					}
					catch(Exception e){
						errors.append(module.getLoadingErrorMessage(e)).append(
								"\n\n");
					}
					
				}
				
			}
			catch(ClassNotFoundException | InstantiationException
					| IllegalAccessException e){
				errors.append("Module \"").append(e.getMessage())
						.append("\" not found.").append("\n");
			}
			
		}
		
		if(errors.length() != 0){
			throw new Exception(errors.toString());
		}
		
	}
	
	private static void setupGlobalVariables(boolean isDebugging){
		
		IS_RUNNING_FROM_TERMINAL = getIsRunningFromTerminal();
		RUNNABLE_SYSTEM_PATH = getRunnableFolderPath();
		BUILD_STARTED_AT = getBuildStartedAt();
		IS_DEBUGGING = isDebugging;
		
	}
	
	private static boolean getIsRunningFromTerminal(){
		return System.console() != null || GraphicsEnvironment.isHeadless();
	}
	
	private static String getRunnableFolderPath(){
		
		String systemPath = Framework.classRunIn.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		
		try{
			systemPath = URLDecoder.decode(systemPath, "UTF-8");
		}
		catch(UnsupportedEncodingException e){}
		
		File runner = new File(systemPath);
		
		return runner.getParent() + File.separator;
		
	}
	
	private static Date getBuildStartedAt(){
		return new Date();
	}
	
}
