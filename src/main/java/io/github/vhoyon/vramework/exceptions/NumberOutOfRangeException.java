package io.github.vhoyon.vramework.exceptions;

public class NumberOutOfRangeException extends IndexOutOfBoundsException {
	
	public NumberOutOfRangeException(){
		super();
	}
	
	public NumberOutOfRangeException(String message){
		super(message);
	}
	
}
