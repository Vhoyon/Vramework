package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;

public abstract class AbstractTerminalCommand implements LinkableCommand {
	
	public AbstractTerminalConsole console;
	
	public void setConsole(AbstractTerminalConsole console){
		this.console = console;
	}
	
	public boolean doesStopTerminal(){
		return false;
	}
	
}
