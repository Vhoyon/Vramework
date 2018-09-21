package io.github.vhoyon.objects;

import io.github.vhoyon.abstracts.CommandsLinker;

public class CommandsRepository {
	
	private Dictionary dict;
	
	private CommandsLinker commandsLinker;
	
	public CommandsRepository(CommandsLinker commandLinker){
		this.commandsLinker = commandLinker;
	}
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
		commandsLinker.setDictionary(dict);
	}
	
	public CommandLinksContainer getContainer(){
		return commandsLinker.getContainer();
	}
	
	public String getFullHelpString(String textHeader){
		return commandsLinker.getFullHelpString(textHeader);
	}
	
	public String getFullHelpString(String textHeader, boolean showDescriptions){
		return commandsLinker.getFullHelpString(textHeader, showDescriptions);
	}
	
	public String getFullHelpString(String textHeader,
			boolean showDescriptions, boolean shouldSummarize){
		return commandsLinker.getFullHelpString(textHeader, showDescriptions,
				shouldSummarize);
	}
	
}
