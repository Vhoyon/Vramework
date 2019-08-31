package io.github.vhoyon.vramework.util;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
	
	private final LinkedBlockingQueue<Runnable> queue;
	
	private final Class<?> ownerClass;
	
	private PoolWorker[] workers;
	
	public ThreadPool(){
		this(1);
	}
	
	public ThreadPool(int nThreads){
		this.queue = new LinkedBlockingQueue<>();
		this.workers = new PoolWorker[nThreads];
		
		StackTraceElement[] stackElements = Thread.currentThread()
				.getStackTrace();
		
		String className = null;
		int lineNumber = -1;
		
		for(StackTraceElement stackElement : stackElements){
			
			if(!stackElement.equals(stackElements[0])
					&& !stackElement.getClassName().equals(
							ThreadPool.class.getCanonicalName())){
				
				className = stackElement.getClassName();
				lineNumber = stackElement.getLineNumber();
				
				break;
				
			}
			
		}
		
		try{
			ownerClass = Class.forName(className);
		}
		catch(ClassNotFoundException e){
			throw new RuntimeException(
					"You have hit an exception that should never be triggered, please review the state of your application.",
					e);
		}
		
		for(int i = 0; i < nThreads; i++){
			this.workers[i] = new PoolWorker();
			this.workers[i].setName("ThreadPool_" + className + lineNumber
					+ "_" + i);
			this.workers[i].setDaemon(true);
			this.workers[i].start();
		}
	}
	
	public void execute(Runnable task){
		synchronized(queue){
			this.queue.add(task);
			this.queue.notify();
		}
	}
	
	public void stopWorkers(){
		for(int i = 0; i < this.workers.length; i++){
			this.workers[i].interrupt();
			this.workers[i] = null;
		}
	}
	
	private class PoolWorker extends Thread {
		
		@Override
		public void run(){
			Runnable task;
			
			while(!this.isInterrupted()){
				synchronized(queue){
					while(queue.isEmpty()){
						try{
							queue.wait();
						}
						catch(InterruptedException e){}
					}
					task = queue.poll();
				}
				
				// If we don't catch RuntimeException,
				// the pool could leak workers
				try{
					task.run();
					if(ownerClass != null)
						synchronized(ownerClass){
							ownerClass.notifyAll();
						}
				}
				catch(RuntimeException e){}
				finally{
					task = null;
				}
			}
		}
		
	}
	
}