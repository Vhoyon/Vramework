package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.interfaces.LinkableMultiCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandExit extends AbstractTerminalCommand implements
		LinkableMultiCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"exit", "off"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();
			
			console.onExit();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
	@Override
	public boolean doesStopTerminal(){
		return true;
	}
	
}
