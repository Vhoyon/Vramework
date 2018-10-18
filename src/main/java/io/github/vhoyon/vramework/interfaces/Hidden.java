package io.github.vhoyon.vramework.interfaces;

public interface Hidden {
	
	/**
	 * Determines if the command should be hidden or not to allow to be hidden
	 * dynamically. Defaults to {@code true} without any checks.
	 * 
	 * @return {@code true} if this command should be hidden, {@code false}
	 *         otherwise.
	 */
	default boolean hiddenCondition(){
		return true;
	}
	
}
