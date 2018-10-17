package io.github.vhoyon.vramework.utilities;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerManager {

	private static HashMap<String,Timer> timers;

	private TimerManager(){}

	/**
	 * @param timerName Name of the timer
	 * @param delay Delay (in milliseconds) before doing the action
	 * @param action Action to do after the delay was finished, without being called again, which resets the delay
	 */
	public static void manage(final String timerName, final int delay, TimerTask action){

		if(timers == null)
			timers = new HashMap<>();
		
		stopTimer(timerName);
		
		Timer timer = new Timer(timerName);
		
		timer.schedule(action, delay);


		timers.put(timerName,timer);
		

	}

	public static void stopTimer(String timerName){

		if(timers.containsKey(timerName))
			timers.get(timerName).cancel();

	}

}
