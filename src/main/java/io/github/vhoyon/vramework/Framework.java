package io.github.vhoyon.vramework;

import io.github.vhoyon.vramework.abstracts.Module;
import io.github.vhoyon.vramework.modules.*;

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
	
	private static Class[] defaultModules = new Class[]
	{
		Environment.class, Logger.class, Metrics.class, Audit.class
	};
	
	public static void build(Class<? extends Module>... modulesToLoad)
			throws Exception{
		Framework.build(false, modulesToLoad);
	}
	
	public static void build(boolean isDebugging,
			Class<? extends Module>... modulesToLoad) throws Exception{
		
		final StackTraceElement[] stackElements = Thread.currentThread()
				.getStackTrace();
		
		for(int i = 0; i < stackElements.length; i++){
			
			if(!stackElements[i].getClassName().equals(
					Framework.class.getCanonicalName())){
				
				Framework.classRunIn = Class.forName(stackElements[i]
						.getClassName());
				break;
				
			}
			
		}
		
		Framework.setupGlobalVariables(isDebugging);
		
		StringBuilder errors = new StringBuilder();
		
		for(Class<?> defaultModule : defaultModules){
			
			if(!Module.class.isAssignableFrom(defaultModule))
				continue;
			
			try{
				
				Module defaultModuleToLoad = (Module)defaultModule
						.newInstance();
				
				try{
					defaultModuleToLoad.build();
				}
				catch(Exception e){
					errors.append(defaultModuleToLoad.getLoadingErrorMessage(e))
							.append("\n\n");
				}
				
			}
			catch(InstantiationException | IllegalAccessException e){
				errors.append("Module \"").append(e.getMessage())
						.append("\" not found.").append("\n");
			}
			
		}
		
		for(Class<? extends Module> module : modulesToLoad){
			
			try{
				
				Module moduleToLoad = module.newInstance();
				
				try{
					moduleToLoad.build();
				}
				catch(Exception e){
					errors.append(moduleToLoad.getLoadingErrorMessage(e))
							.append("\n\n");
				}
				
			}
			catch(InstantiationException | IllegalAccessException e){
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
