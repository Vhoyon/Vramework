package io.github.vhoyon.vramework.utilities;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerManager {
	
	private static HashMap<String, TimerWrapper> timers;
	
	private static class TimerWrapper extends Timer {
		public int delay;
		public long timeStarted;
		
		public TimerWrapper(String timerName, int delay){
			super(timerName);
			this.delay = delay;
			timeStarted = System.currentTimeMillis();
		}
		
		public int getTimeRemaining(){
			return delay
					- ((int)(System.currentTimeMillis() - timeStarted));
		}
	}
	
	private TimerManager(){}
	
	/**
	 * @param timerName
	 *            Name of the timer
	 * @param delay
	 *            Delay (in milliseconds) before doing the action
	 * @param action
	 *            Action to do after the delay was finished, without being
	 *            called again, which resets the delay
	 */
	public static void schedule(final String timerName, final int delay,
			Runnable action){
		schedule(timerName, delay, action, null);
	}
	
	/**
	 * @param timerName
	 *            Name of the timer
	 * @param delay
	 *            Delay (in milliseconds) before doing the action
	 * @param action
	 *            Action to do after the delay was finished, without being
	 *            called again, which resets the delay
	 * @param handler
	 *            Handler object that gets notified when the delay is over
	 */
	public static void schedule(final String timerName, final int delay,
			Runnable action, final Object handler){
		
		if(timers == null)
			timers = new HashMap<>();
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				action.run();
				
				stopTimer(timerName, handler);
			}
		};
		
		stopTimer(timerName);
		
		TimerWrapper timer = new TimerWrapper(timerName, delay);
		
		timer.schedule(task, delay);
		
		timers.put(timerName, timer);
		
	}
	
	public static void stopTimer(String timerName){
		stopTimer(timerName, null);
	}
	
	public static void stopTimer(String timerName, Object handler){
		
		if(timers != null && timers.containsKey(timerName)){
			timers.remove(timerName).cancel();
			
			if(handler != null){
				synchronized(handler){
					handler.notifyAll();
				}
			}
		}
		
	}
	
	public static int getTimeRemaining(String timerName)
			throws NullPointerException{
		return timers.get(timerName).getTimeRemaining();
	}
	
}
