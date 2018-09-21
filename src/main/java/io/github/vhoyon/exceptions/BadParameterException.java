package io.github.vhoyon.exceptions;

public class BadParameterException extends Exception {
	public BadParameterException(){
		super();
	}
	
	public BadParameterException(String explanation){
		super(explanation);
	}
}
