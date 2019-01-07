package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.interfaces.LinkableMultiCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	
	public Link(Class<? extends LinkableCommand> command){
		this.classToLink = command;
	}
	
	public LinkableCommand getInstance() throws IllegalStateException{
		try{
			return getClassToLink().newInstance();
		}
		catch(IllegalAccessException | InstantiationException e){
			throw new IllegalStateException(
					"The class to link is not accessible nor instantiable!", e);
		}
	}
	
	public boolean hasCall(String call){
		
		if(call != null && call.length() != 0){
			
			LinkableCommand instance = getInstance();
			
			if(instance instanceof LinkableMultiCommand){
				
				String[] calls = ((LinkableMultiCommand)instance).getCalls();
				
				for(String definedCall : calls)
					if(definedCall.equals(call))
						return true;
				
			}
			else{
				
				String linkCall = instance.getCall();
				
				return call.equals(linkCall);
				
			}
			
		}
		
		return false;
		
	}
	
	public String getCall(){
		try{
			return getInstance().getCall();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public String getDefaultCall(){
		return this.getCall();
	}
	
	public Class<? extends LinkableCommand> getClassToLink(){
		return this.classToLink;
	}
	
}
