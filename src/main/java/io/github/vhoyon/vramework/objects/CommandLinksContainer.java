package io.github.vhoyon.vramework.objects;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.vhoyon.vramework.exceptions.CommandNotFoundException;
import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.modules.Logger;

public abstract class CommandLinksContainer {
	
	private Map<String, Link> linkMap;
	
	/**
	 * The latest links commands will always replace the first command call.
	 */
	public CommandLinksContainer(Link... links){
		initializeContainer(links);
	}
	
	/**
	 * The latest links commands will always replace the first command call.
	 */
	@SafeVarargs
	public CommandLinksContainer(Class<? extends LinkableCommand>... commands){
		
		Link[] linksArray = Arrays.stream(commands).map(Link::new)
				.toArray(Link[]::new);
		
		initializeContainer(linksArray);
		
	}
	
	public CommandLinksContainer(String linksPackage){
		
		try(ScanResult results = new ClassGraph().whitelistPackages(
				linksPackage).scan()){
			
			List<Class<LinkableCommand>> linkableCommands = results
					.getClassesImplementing(
							LinkableCommand.class.getCanonicalName())
					.loadClasses(LinkableCommand.class);
			
			Link[] links = linkableCommands.stream().map(Link::new)
					.toArray(Link[]::new);
			
			initializeContainer(links);
			
		}
		
	}
	
	private void initializeContainer(Link[] links){
		
		for(Link link : links){
			
			try{
				
				List<String> calls = link.getInstance().getAllCalls();
				
				Map<String, Link> linkMap = this.getLinkMap();
				
				calls.forEach(call -> linkMap.put(call, link));
				
			}
			catch(IllegalStateException e){}
			
		}
		
	}
	
	public Map<String, Link> getLinkMap(){
		if(this.linkMap == null){
			this.linkMap = new LinkedHashMap<>();
		}
		
		return this.linkMap;
	}
	
	public LinkableCommand initiateLink(String commandName){
		try{
			
			Link link = findLink(commandName);
			
			if(link != null){
				return link.getInstance();
			}
			
		}
		catch(Exception e){
			Logger.log(e);
		}
		
		return this.whenCommandNotFound(commandName);
	}
	
	public abstract LinkableCommand whenCommandNotFound(String commandName);
	
	public Link findLink(String commandName){
		return this.getLinkMap().get(commandName);
	}
	
	public LinkableCommand findCommand(String commandName)
			throws CommandNotFoundException{
		
		Link link = this.findLink(commandName);
		
		if(link == null){
			throw new CommandNotFoundException(commandName);
		}
		else{
			return link.getInstance();
		}
		
	}
	
}
