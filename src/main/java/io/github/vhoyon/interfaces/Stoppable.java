package io.github.vhoyon.interfaces;

public interface Stoppable {
	
	default boolean stopMiddleware(){
		return true;
	}
	
}
