package io.github.vhoyon.vramework.interfaces;

import java.util.Arrays;

public interface LinkableMultiCommand extends LinkableCommand {
	
	@Override
	default String getCall(){
		return getActualCall();
	}
	
	String[] getCalls();
	
	default String getDefaultCall(){
		String[] calls = getCalls();
		
		if(calls == null || calls.length == 0)
			throw new IllegalStateException(
					"The calls should not be null / empty - please check that your command has its calls setup correctly!");
		
		return calls[0];
	}
	
	default String getActualCall(){
		return this.getDefaultCall();
	}
	
	default String[] getCallsExceptActual(){
		return Arrays.stream(getCalls())
				.filter(o -> !getActualCall().equals(o)).toArray(String[]::new);
	}
	
}
