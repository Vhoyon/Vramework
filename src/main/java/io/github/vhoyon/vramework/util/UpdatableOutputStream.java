package io.github.vhoyon.vramework.util;

import java.io.PrintStream;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public abstract class UpdatableOutputStream extends PrintStream {
	
	public enum Type{
		OUT, ERR
	}
	
	private PrintStream originalPrintStream;
	private Type outputType;
	
	private Thread loggingThread;
	
	private boolean isWaitingForInput;
	private boolean isPrinting;
	
	private String latestInputMessage;
	
	private int delayedUpdate;
	
	public UpdatableOutputStream(PrintStream sysOutput){
		// this(sysOutput, detectPrintStream(sysOutput));
		super(sysOutput);
		this.originalPrintStream = sysOutput;
		
		this.setIsWaitingForInput(false);
		this.setIsPrinting(false);
		this.setDelayedUpdate(250);
		
		this.outputType = detectPrintStream(sysOutput);
		
		this.updateSystemOutput(this);
	}
	
	private static Type detectPrintStream(PrintStream sysOutput){
		if(System.out.equals(sysOutput))
			return Type.OUT;
		else if(System.err.equals(sysOutput))
			return Type.ERR;
		
		throw new RuntimeException(
				"Parameter sysOutput is not a System PrintStream object : you should only use System.out and System.err!");
	}
	
	public UpdatableOutputStream(PrintStream sysOutput, Type outputType){
		super(sysOutput);
		this.originalPrintStream = sysOutput;
		
		this.setIsWaitingForInput(false);
		this.setIsPrinting(false);
		this.setDelayedUpdate(250);
		
		if(outputType != null){
			this.outputType = outputType;
			
			this.updateSystemOutput(this);
		}
	}
	
	public void setIsWaitingForInput(boolean isWaitingForInput){
		this.isWaitingForInput = isWaitingForInput;
	}
	
	public void setIsPrinting(boolean isPrinting){
		this.isPrinting = isPrinting;
		
		if(!isPrinting && this.loggingThread != null)
			this.loggingThread = null;
	}
	
	public void setDelayedUpdate(int delayedUpdate){
		this.delayedUpdate = delayedUpdate;
	}
	
	public boolean isWaitingForInput(){
		return this.isWaitingForInput;
	}
	
	public boolean isPrinting(){
		return this.isPrinting;
	}
	
	public int getDelayedUpdate(){
		return this.delayedUpdate;
	}
	
	public String getLatestInputMessage(){
		return this.latestInputMessage;
	}
	
	public void setLatestInputMessage(String message){
		this.latestInputMessage = message;
	}
	
	public UpdatableOutputStream resetStream(){
		this.updateSystemOutput(this.originalPrintStream);
		this.outputType = null;
		
		return this;
	}
	
	protected void updateSystemOutput(PrintStream printStream){
		switch(this.outputType){
		default:
		case OUT:
			System.setOut(printStream);
			break;
		case ERR:
			System.setErr(printStream);
			break;
		}
	}
	
	public abstract String getUpdatingString();
	
	public abstract String formatLatestInputMessage(String latestMessage);
	
	private void handlePrint(Runnable action){
		
		if(this.isWaitingForInput() && !this.isPrinting())
			super.print(getUpdatingString() + "\n");
		
		this.setIsPrinting(true);
		
		action.run();
		
		if(!this.isWaitingForInput()){
			this.setIsPrinting(false);
		}
		else{
			
			if(loggingThread != null)
				loggingThread.interrupt();
			
			loggingThread = new Thread(
					() -> {
						
						try{
							Thread.sleep(getDelayedUpdate());
							
							if(isWaitingForInput()){
								
								String latestInputMessage = formatLatestInputMessage(getLatestInputMessage());
								
								if(outputType == Type.ERR){
									
									if(System.out instanceof UpdatableOutputStream){
										((UpdatableOutputStream)System.out)
												.refreshLatestInputMessage(latestInputMessage);
									}
									else{
										System.out.print(latestInputMessage);
									}
									
								}
								else{
									super.print(latestInputMessage);
								}
								
								setIsPrinting(false);
								
							}
						}
						catch(InterruptedException e){}
						
					});
			
			loggingThread.start();
			
		}
		
	}
	
	public void refreshLatestInputMessage(String latestInputMessage){
		this.setIsWaitingForInput(false);
		this.print(latestInputMessage);
		this.setIsWaitingForInput(true);
	}
	
	// Prints overrides to handle updates via default methods
	
	@Override
	public void print(final String s){
		handlePrint(() -> super.print(s));
	}
	
	@Override
	public void print(final boolean b){
		handlePrint(() -> super.print(b));
	}
	
	@Override
	public void print(final char c){
		handlePrint(() -> super.print(c));
	}
	
	@Override
	public void print(final char[] s){
		handlePrint(() -> super.print(s));
	}
	
	@Override
	public void print(final double d){
		handlePrint(() -> super.print(d));
	}
	
	@Override
	public void print(final float f){
		handlePrint(() -> super.print(f));
	}
	
	@Override
	public void print(final int i){
		handlePrint(() -> super.print(i));
	}
	
	@Override
	public void print(final long l){
		handlePrint(() -> super.print(l));
	}
	
	@Override
	public void print(final Object obj){
		handlePrint(() -> super.print(obj));
	}
	
	@Override
	public PrintStream format(final Locale l, final String format,
			final Object... args){
		AtomicReference<PrintStream> returnVal = new AtomicReference<>();
		
		handlePrint(() -> returnVal.set(super.format(l, format, args)));
		
		return returnVal.get();
	}
	
	@Override
	public PrintStream format(final String format, final Object... args){
		AtomicReference<PrintStream> returnVal = new AtomicReference<>();
		
		handlePrint(() -> returnVal.set(super.format(format, args)));
		
		return returnVal.get();
	}
	
	@Override
	public void write(final int b){
		handlePrint(() -> super.write(b));
	}
	
}