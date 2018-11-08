package io.github.vhoyon.vramework.exceptions;

public class AmountNotDefinedException extends RuntimeException {
	
	public AmountNotDefinedException(int amount){
		super("The amount " + amount + " is not specified in any lang lines given.");
	}
	
}
