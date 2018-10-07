package io.github.vhoyon.vramework.exceptions;

public class BadFormatException extends IllegalArgumentException {
	
	private int code;
	
	public static final int DEFAULT_ERROR_CODE = -1;
	
	public BadFormatException(){
		this(DEFAULT_ERROR_CODE);
	}
	
	public BadFormatException(int errorCode){
		super();
		
		this.code = errorCode;
	}
	
	public BadFormatException(String message){
		this(message, DEFAULT_ERROR_CODE);
	}
	
	public BadFormatException(String message, int errorCode){
		super(message);
		
		this.code = errorCode;
	}
	
	public int getErrorCode(){
		return this.code;
	}
	
}
