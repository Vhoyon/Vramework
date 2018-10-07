package io.github.vhoyon.vramework.interfaces;

public interface Stoppable {
	
	default boolean stopMiddleware(){
		return true;
	}
	
}
