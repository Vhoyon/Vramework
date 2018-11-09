package io.github.vhoyon.vramework.abstracts;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import io.github.vhoyon.vramework.interfaces.Console;

public abstract class AbstractUIConsole extends JFrame implements Console {
	
	public AbstractUIConsole(){
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				try{
					onStop();
				}
				catch(Exception e1){}
				
				super.windowClosing(e);
			}
		});
		
		setLocationRelativeTo(null);
	}
	
	@Override
	public String getInput(String message){
		return JOptionPane.showInputDialog(this, message);
	}
	
	@Override
	public int getConfirmation(String question, QuestionType questionType){
		
		int jOptionType;
		
		switch(questionType){
		case YES_NO:
			jOptionType = JOptionPane.YES_NO_OPTION;
			break;
		case OK:
			jOptionType = JOptionPane.OK_OPTION;
			break;
		case YES_NO_CANCEL:
		default:
			jOptionType = JOptionPane.YES_NO_CANCEL_OPTION;
			break;
		}
		
		int choice = JOptionPane.showConfirmDialog(this, question,
				"Hey, gotta confirm this!", jOptionType);
		
		return choice;
		
	}
	
	@Override
	public void onStart() throws Exception{}
	
	@Override
	public void onStop() throws Exception{}
	
	@Override
	public void onInitialized(){}
	
	@Override
	public void onExit(){}
	
}
