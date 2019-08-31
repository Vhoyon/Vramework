package io.github.vhoyon.vramework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerManager {
	
	private static Map<String, TimerWrapper> timers;
	
	private static class TimerWrapper extends Timer {
		public int delay;
		public long timeStarted;
		public Runnable action;
		public Runnable doFinally;
		public Runnable onCancel;
		
		public TimerWrapper(String timerName, int delay, Runnable action,
				Runnable doFinally, Runnable onCancel){
			super(timerName);
			this.delay = delay;
			timeStarted = System.currentTimeMillis();
			this.action = action;
			this.doFinally = doFinally;
			this.onCancel = onCancel;
		}
		
		public int getTimeRemaining(){
			return delay - ((int)(System.currentTimeMillis() - timeStarted));
		}
	}
	
	private TimerManager(){}
	
	protected static Map<String, TimerWrapper> getTimers(){
		
		if(timers == null){
			timers = new HashMap<>();
		}
		
		return timers;
		
	}
	
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
		TimerManager.schedule(timerName, delay, action, null);
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
		TimerManager.schedule(timerName, delay, action, doFinally, null);
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
			Runnable action, Runnable doFinally, Runnable onCancel){
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				if(action != null){
					action.run();
				}
				
				TimerManager.stopTimer(timerName, true);
			}
		};
		
		TimerManager.stopTimer(timerName, false);
		
		TimerWrapper timer = new TimerWrapper(timerName, delay, action,
				doFinally, onCancel);
		
		timer.schedule(task, delay);
		
		TimerManager.getTimers().put(timerName, timer);
		
	}
	
	public static boolean resetTimer(String timerName){
		
		if(TimerManager.hasTimer(timerName)){
			TimerWrapper timer = TimerManager.getTimers().get(timerName);
			
			TimerManager.schedule(timerName, timer.delay, timer.action,
					timer.doFinally);
			
			return true;
		}
		
		return false;
		
	}
	
	public static void stopTimer(String timerName){
		TimerManager.stopTimer(timerName, true);
	}
	
	private static void stopTimer(String timerName, boolean isEnd){
		
		if(TimerManager.hasTimer(timerName)){
			TimerWrapper timer = TimerManager.getTimers().remove(timerName);
			
			TimerManager.stopTimer(timer, isEnd);
		}
		
	}
	
	private static void stopTimer(TimerWrapper timer, boolean isEnd){
		
		timer.cancel();
		
		if(isEnd && timer.doFinally != null){
			timer.doFinally.run();
		}
		
	}
	
	public static void cancelTimer(String timerName){
		TimerManager.cancelTimer(timerName, true);
	}
	
	public static void cancelTimer(String timerName, boolean shouldRunFinalActions){
		
		if(TimerManager.hasTimer(timerName)){
			TimerWrapper timer = TimerManager.getTimers().remove(timerName);
			
			TimerManager.stopTimer(timer, shouldRunFinalActions);
			
			timer.onCancel.run();
		}
		
	}
	
	public static boolean hasTimer(String timerName){
		Map<String, TimerWrapper> timers = TimerManager.getTimers();
		return timers != null && timers.containsKey(timerName);
	}
	
	public static void stopAllTimers(){
		
		Map<String, TimerWrapper> timers = TimerManager.getTimers();
		
		if(timers.isEmpty()){
			return;
		}
		
		timers.forEach((s, timer) -> timer.cancel());
		
		timers.clear();
		
	}
	
	public static int getTimeRemaining(String timerName)
			throws NullPointerException{
		return TimerManager.getTimers().get(timerName).getTimeRemaining();
	}
	
}
