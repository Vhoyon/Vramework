package io.github.vhoyon.exceptions;

public class NoContentException extends Exception {
	public NoContentException(String commandName){
		super("No content has been set for the command " + commandName + ".");
	}
}
