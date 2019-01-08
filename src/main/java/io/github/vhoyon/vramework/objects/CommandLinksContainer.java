package io.github.vhoyon.vramework.objects;

import java.util.LinkedHashMap;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.vhoyon.vramework.exceptions.CommandNotFoundException;
import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.modules.Logger;

public abstract class CommandLinksContainer {
	
	private LinkedHashMap<String, Link> linkMap;
	
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
		Link[] links = new Link[commands.length];
		
		for(int i = 0; i < commands.length; i++){
			links[i] = new Link(commands[i]);
		}
		
		initializeContainer(links);
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
			
			Class<? extends LinkableCommand> linkClass = link.getClassToLink();
			
			if(linkClass.isInterface())
				continue;
			
			String[] calls = link.getInstance().getAllCalls();
			
			for(String call : calls)
				addNewLink(call, link);
			
		}
		
	}
	
	private void addNewLink(String call, Link link){
		
		if(call != null){
			
			if(this.linkMap == null)
				linkMap = new LinkedHashMap<>();
			
			linkMap.put(call, link);
			
		}
		
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
		
		return whenCommandNotFound(commandName);
	}
	
	public abstract LinkableCommand whenCommandNotFound(String commandName);
	
	public LinkedHashMap<String, Link> getLinkMap(){
		return this.linkMap;
	}
	
	public Link findLink(String commandName){
		return getLinkMap().get(commandName);
	}
	
	public LinkableCommand findCommand(String commandName)
			throws CommandNotFoundException{
		
		Link link = findLink(commandName);
		
		if(link == null){
			throw new CommandNotFoundException(commandName);
		}
		else{
			return link.getInstance();
		}
		
	}
	
}
