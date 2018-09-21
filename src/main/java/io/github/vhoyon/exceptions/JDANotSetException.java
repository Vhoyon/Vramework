package io.github.vhoyon.exceptions;

public class JDANotSetException extends Exception {
	public JDANotSetException(){
		super("The JDA is not set!");
	}
}
