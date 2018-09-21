package io.github.vhoyon.objects;

import io.github.vhoyon.interfaces.LinkableCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	
	public Link(Class<? extends LinkableCommand> command){
		this.classToLink = command;
	}
	
	public LinkableCommand getInstance() throws Exception{
		return getClassToLink().newInstance();
	}
	
	public boolean hasCall(String call){
		
		if(call != null && call.length() != 0){
			
			if(getCalls() instanceof String[]){
				
				String[] calls = (String[])getCalls();
				
				for(String definedCall : calls)
					if(definedCall.equals(call))
						return true;
				
			}
			else{
				
				String linkCall = getCalls().toString();
				
				return call.equals(linkCall);
				
			}
			
		}
		
		return false;
		
	}
	
	public Object getCalls(){
		try{
			return getInstance().getCalls();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public String getDefaultCall(){
		try{
			return getInstance().getDefaultCall();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public Class<? extends LinkableCommand> getClassToLink(){
		return this.classToLink;
	}
	
}
