package io.github.vhoyon.ui;

import io.github.vhoyon.abstracts.AbstractUIConsole;
import io.github.vhoyon.modules.Logger;
import io.github.vhoyon.objects.NotificationLogger;


public class NotificationUI extends AbstractUIConsole {

	private NotificationLogger logger;

	@Override
	public void initialize(){

		logger = new NotificationLogger(this);

		Logger.setOutputs(logger);

	}

}
