package io.github.vhoyon.exceptions;

public class BadFileContentException extends Exception {
	public BadFileContentException(){
		super();
	}
	
	public BadFileContentException(String explanation){
		super(explanation);
	}
}
