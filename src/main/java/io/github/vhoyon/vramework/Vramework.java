package io.github.vhoyon.vramework;

import java.awt.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.vhoyon.vramework.abstracts.Module;
import io.github.vhoyon.vramework.exceptions.ModuleLoaderException;
import io.github.vhoyon.vramework.modules.Audit;
import io.github.vhoyon.vramework.modules.Environment;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Metrics;

public class Vramework {
	
	private static Class<?> classRunIn;
	
	private static boolean IS_RUNNING_FROM_TERMINAL;
	private static String RUNNABLE_SYSTEM_PATH;
	private static Date BUILD_STARTED_AT;
	private static boolean IS_DEBUGGING;
	
	@SuppressWarnings("rawtypes")
	private static Class[] defaultModules = new Class[]
	{
		Environment.class, Logger.class, Metrics.class, Audit.class
	};
	
	private Vramework(){}
	
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
	
	@SafeVarargs
	public static void build(Class<? extends Module>... modulesToLoad)
			throws ModuleLoaderException{
		Vramework.build(false, modulesToLoad);
	}
	
	@SafeVarargs
	public static void build(boolean isDebugging,
			Class<? extends Module>... modulesToLoad)
			throws ModuleLoaderException{
		Vramework.build(isDebugging, true, modulesToLoad);
	}
	
	@SafeVarargs
	public static void buildClean(Class<? extends Module>... modulesToLoad)
			throws Exception{
		Vramework.buildClean(false, modulesToLoad);
	}
	
	@SafeVarargs
	public static void buildClean(boolean isDebugging,
			Class<? extends Module>... modulesToLoad)
			throws ModuleLoaderException{
		Vramework.build(isDebugging, false, modulesToLoad);
	}
	
	@SafeVarargs
	public static void build(boolean isDebugging,
			boolean shouldLoadDefaultModules,
			Class<? extends Module>... modulesToLoad)
			throws ModuleLoaderException{
		
		final StackTraceElement[] stackElements = Thread.currentThread()
				.getStackTrace();
		
		for(StackTraceElement stackElement : stackElements){
			
			if(stackElement == stackElements[0])
				continue;
			
			if(!stackElement.getClassName().equals(
					Vramework.class.getCanonicalName())){
				
				try{
					Vramework.classRunIn = Class.forName(stackElement
							.getClassName());
				}
				catch(ClassNotFoundException e){
					// This error should never happen.
					throw new IllegalStateException(
							"Unexpected error in StackTrace handling. Please review the state of your java installation.");
				}
				
				break;
				
			}
			
		}
		
		Vramework.setupGlobalVariables(isDebugging);
		
		List<String> defaultModuleErrors = null;
		if(shouldLoadDefaultModules){
			defaultModuleErrors = loadDefaultModules();
		}
		List<String> autoModuleErrors = loadAutomaticModules();
		List<String> moduleErrors = loadModules(modulesToLoad);
		
		if((defaultModuleErrors != null && defaultModuleErrors.size() > 0)
				|| autoModuleErrors.size() > 0 || moduleErrors.size() > 0){
			
			List<String> allErrors = new ArrayList<>();
			
			if(defaultModuleErrors != null){
				allErrors.addAll(defaultModuleErrors);
			}
			allErrors.addAll(autoModuleErrors);
			allErrors.addAll(moduleErrors);
			
			StringBuilder sb = new StringBuilder();
			
			for(String error : allErrors){
				sb.append(error).append("\n");
			}
			
			throw new ModuleLoaderException(sb.toString());
			
		}
		
	}
	
	private static List<String> loadDefaultModules(){
		
		List<String> errors = new ArrayList<>();
		
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
					errors.add(defaultModuleToLoad.getLoadingErrorMessage(e));
				}
				
			}
			catch(InstantiationException | IllegalAccessException e){
				errors.add("Module \"" + defaultModule.getCanonicalName()
						+ "\" not found. The Vramework needs an update...");
			}
			
		}
		
		return errors;
		
	}
	
	private static List<String> loadAutomaticModules(){
		
		String packageName = classRunIn.getPackage().getName() + ".modules";
		
		List<Class<Module>> modules;
		
		try(ScanResult results = new ClassGraph()
				.whitelistPackages(packageName).scan()){
			
			modules = results.getSubclasses(Module.class.getCanonicalName())
					.loadClasses(Module.class);
			
		}
		
		List<String> errors = new ArrayList<>();
		
		for(Class<? extends Module> module : modules){
			
			try{
				
				Module moduleToLoad = module.newInstance();
				
				try{
					moduleToLoad.build();
				}
				catch(Exception e){
					errors.add(moduleToLoad.getLoadingErrorMessage(e));
				}
				
			}
			catch(InstantiationException | IllegalAccessException e){
				errors.add("Module \""
						+ module.getCanonicalName()
						+ "\" cannot be instanced. Make sure it has a public constructor with no arguments.");
			}
			
		}
		
		return errors;
		
	}
	
	private static List<String> loadModules(Class<? extends Module>[] modules){
		
		List<String> errors = new ArrayList<>();
		
		for(Class<? extends Module> module : modules){
			
			try{
				
				Module moduleToLoad = module.newInstance();
				
				try{
					moduleToLoad.build();
				}
				catch(Exception e){
					errors.add(moduleToLoad.getLoadingErrorMessage(e));
				}
				
			}
			catch(InstantiationException | IllegalAccessException e){
				if(module.isAnonymousClass()){
					errors.add("Cannot load anonymous modules. Please create a new class to be able to load it.");
				}
				else{
					errors.add("Module \""
							+ module.getCanonicalName()
							+ "\" not found. Make sure it has a public constructor with no arguments.");
				}
			}
			
		}
		
		return errors;
		
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
		
		String systemPath = Vramework.classRunIn.getProtectionDomain()
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
