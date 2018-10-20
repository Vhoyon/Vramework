package io.github.vhoyon.vramework.ui;

import io.github.vhoyon.vramework.abstracts.AbstractUIConsole;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.objects.NotificationLogger;


public class NotificationUI extends AbstractUIConsole {

	private NotificationLogger logger;

	@Override
	public void initialize(boolean startImmediately){

		logger = new NotificationLogger(this);

		Logger.setOutputs(logger);

	}

}
