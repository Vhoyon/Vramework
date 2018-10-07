package io.github.vhoyon.vramework.interfaces;

import javax.swing.JOptionPane;

public interface Console {
	
	enum QuestionType{
		YES_NO, YES_NO_CANCEL
	}

	int YES = JOptionPane.YES_OPTION;
	int NO = JOptionPane.NO_OPTION;
	int CANCEL = JOptionPane.CANCEL_OPTION;

	void onStart() throws Exception;
	
	void onStop() throws Exception;

	void onExit() throws Exception;
	
	void onInitialized();
	
	void initialize();
	
	String getInput(String message);
	
	int getConfirmation(String question, QuestionType questionType);
	
}
