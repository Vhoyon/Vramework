package io.github.vhoyon.vramework.interfaces;

import javax.swing.*;

public interface Console {
	
	enum QuestionType{
		YES_NO, YES_NO_CANCEL, OK
	}
	
	int YES = JOptionPane.YES_OPTION;
	int NO = JOptionPane.NO_OPTION;
	int CANCEL = JOptionPane.CANCEL_OPTION;
	int OK = JOptionPane.OK_OPTION;
	
	void onStart() throws Exception;
	
	void onStop() throws Exception;
	
	void onExit() throws Exception;
	
	void onInitialized();
	
	default void initialize(){
		initialize(false);
	}
	
	void initialize(boolean startImmediately);
	
	String getInput(String message);
	
	int getConfirmation(String question, QuestionType questionType);
	
}
