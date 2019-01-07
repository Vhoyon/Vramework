package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandStart extends AbstractTerminalCommand {
	
	@Override
	public String getCall(){
		return "start";
	}
	
	@Override
	public void action(){
		
		try{
			console.onStart();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
