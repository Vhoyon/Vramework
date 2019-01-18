package io.github.vhoyon.vramework.objects;

import javax.swing.*;

import io.github.vhoyon.vramework.interfaces.Loggable;

public class NotificationLogger implements Loggable {
	
	private JFrame frame;
	
	public NotificationLogger(){
		frame = new JFrame();
		frame.setLocationRelativeTo(null);
	}
	
	public NotificationLogger(JFrame frame){
		this.frame = frame;
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		JOptionPane.showMessageDialog(frame, logText);
	}
	
}
