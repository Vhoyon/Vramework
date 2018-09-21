package io.github.vhoyon.objects;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.abstracts.CommandsLinker;
import io.github.vhoyon.interfaces.LinkableCommand;
import io.github.vhoyon.modules.Logger;

public class TerminalCommandsLinker extends CommandsLinker {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer("vendor.io.github.vhoyon.terminalcommands"){
			
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
