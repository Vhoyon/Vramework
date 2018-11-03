package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.abstracts.CommandsLinker;
import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandsLinker extends CommandsLinker {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer(
				"io.github.vhoyon.vramework.terminalcommands"){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new AbstractTerminalCommand(){
					@Override
					public String[] getCalls(){
						return null;
					}
					
					@Override
					public void action(){
						Logger.log("No command with the name \"" + commandName
								+ "\"!", Logger.LogType.WARNING, false);
					}
				};
				
			}
			
		};
		
	}
	
}
