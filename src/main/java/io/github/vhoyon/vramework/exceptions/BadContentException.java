package io.github.vhoyon.vramework.exceptions;

public class BadContentException extends Exception {
	public BadContentException(){
		super();
	}
	
	public BadContentException(String explanation){
		super(explanation);
	}
}
