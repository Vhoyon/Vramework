package io.github.vhoyon.vramework.exceptions;

public class BadParameterException extends Exception {
	public BadParameterException(){
		super();
	}
	
	public BadParameterException(String explanation){
		super(explanation);
	}
}
