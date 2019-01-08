package io.github.vhoyon.vramework.utilities;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerManager {
	
	private static HashMap<String, TimerWrapper> timers;
	
	private static class TimerWrapper extends Timer {
		public int delay;
		public long timeStarted;
		public Runnable action;
		public Runnable doFinally;
		
		public TimerWrapper(String timerName, int delay, Runnable action,
				Runnable doFinally){
			super(timerName);
			this.delay = delay;
			timeStarted = System.currentTimeMillis();
			this.action = action;
			this.doFinally = doFinally;
		}
		
		public int getTimeRemaining(){
			return delay - ((int)(System.currentTimeMillis() - timeStarted));
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
	 * @param doFinally
	 *            Action to do when everything is done (including when
	 *            stopping this timer).
	 */
	public static void schedule(final String timerName, final int delay,
			Runnable action, Runnable doFinally){
		
		if(timers == null)
			timers = new HashMap<>();
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				if(action != null)
					action.run();
				
				stopTimer(timerName);
			}
		};
		
		stopTimer(timerName, false);
		
		TimerWrapper timer = new TimerWrapper(timerName, delay, action,
				doFinally);
		
		timer.schedule(task, delay);
		
		timers.put(timerName, timer);
		
	}
	
	public static boolean resetTimer(String timerName){
		
		if(timers != null && timers.containsKey(timerName)){
			TimerWrapper timer = timers.get(timerName);
			
			TimerManager.schedule(timerName, timer.delay, timer.action,
					timer.doFinally);
			
			return true;
		}
		
		return false;
		
	}
	
	public static void stopTimer(String timerName){
		stopTimer(timerName, true);
	}
	
	private static void stopTimer(String timerName, boolean shouldDoFinally){
		
		if(timers != null && timers.containsKey(timerName)){
			TimerWrapper timer = timers.remove(timerName);
			
			timer.cancel();
			
			if(shouldDoFinally && timer.doFinally != null){
				timer.doFinally.run();
			}
		}
		
	}
	
	public static int getTimeRemaining(String timerName)
			throws NullPointerException{
		return timers.get(timerName).getTimeRemaining();
	}
	
}
