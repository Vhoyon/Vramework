package io.github.vhoyon.vramework.exceptions;

public class BadOptionException extends Exception {
	public BadOptionException(){
		super();
	}
	
	public BadOptionException(String explanation){
		super(explanation);
	}
}
