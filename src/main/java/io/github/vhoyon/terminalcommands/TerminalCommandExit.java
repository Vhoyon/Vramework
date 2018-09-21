package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;

public class TerminalCommandExit extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"exit"
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
	public boolean doesStopTerminal() {
		return true;
	}

}
