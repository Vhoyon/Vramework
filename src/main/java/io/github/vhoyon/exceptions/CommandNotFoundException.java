package io.github.vhoyon.exceptions;

public class CommandNotFoundException extends Exception {
	
	public CommandNotFoundException(String commandName){
		super("Command with call \"" + commandName + "\" does not exists.");
	}
	
}
