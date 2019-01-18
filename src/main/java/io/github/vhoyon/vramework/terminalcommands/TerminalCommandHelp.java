package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandHelp extends AbstractTerminalCommand {
	
	@Override
	public String getCall(){
		return "help";
	}
	
	@Override
	public void action(){
		
		String fullHelpString = console.getCommandsRepo().getFullHelpString(
				"Available commands :");
		
		Logger.log(fullHelpString, false);
		
	}
	
}
