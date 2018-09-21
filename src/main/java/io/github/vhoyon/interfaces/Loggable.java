package io.github.vhoyon.interfaces;

public interface Loggable extends Outputtable {
	
	void log(String logText, String logType, boolean hasAppendedDate);
	
}
