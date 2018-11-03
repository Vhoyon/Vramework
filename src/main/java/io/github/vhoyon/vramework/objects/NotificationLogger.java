package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.interfaces.Loggable;

import javax.swing.*;

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
