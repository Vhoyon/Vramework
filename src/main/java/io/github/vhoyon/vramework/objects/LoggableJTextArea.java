package io.github.vhoyon.vramework.objects;

import javax.swing.*;

import io.github.vhoyon.vramework.interfaces.Loggable;

public class LoggableJTextArea extends JTextArea implements Loggable {
	
	public JScrollPane scrollPane;
	
	private void init(){
		
		setEditable(false);
		setLineWrap(true);
		
		scrollPane = new JScrollPane(this);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
	}
	
	public LoggableJTextArea(){
		super();
		
		this.init();
	}
	
	public LoggableJTextArea(int rows, int columns){
		super(rows, columns);
		
		this.init();
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		append(logText);
		
		append("\n");
		
		setCaretPosition(getDocument().getLength());
	}
	
}
