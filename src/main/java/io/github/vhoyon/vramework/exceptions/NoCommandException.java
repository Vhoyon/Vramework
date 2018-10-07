package io.github.vhoyon.vramework.exceptions;

public class NoCommandException extends Exception {
	public NoCommandException(){
		super("The message received is not a command.");
	}
}