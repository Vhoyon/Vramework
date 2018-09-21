package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;

public class TerminalCommandHelp extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"help"
		};
	}
	
	@Override
	public void action(){

		String fullHelpString = console.getCommandsRepo().getFullHelpString("Available commands :");

		Logger.log(fullHelpString, false);

	}
	
}
