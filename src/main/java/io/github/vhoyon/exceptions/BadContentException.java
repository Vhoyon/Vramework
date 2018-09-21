package io.github.vhoyon.exceptions;

public class BadContentException extends Exception {
	public BadContentException(){
		super();
	}
	
	public BadContentException(String explanation){
		super(explanation);
	}
}
