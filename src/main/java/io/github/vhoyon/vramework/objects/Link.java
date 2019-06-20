package io.github.vhoyon.vramework.objects;

import java.util.List;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	
	public Link(Class<? extends LinkableCommand> command){
		this.classToLink = command;
	}
	
	public LinkableCommand getInstance() throws IllegalStateException{
		try{
			return this.getClassToLink().newInstance();
		}
		catch(IllegalAccessException | InstantiationException e){
			throw new IllegalStateException(
					"The class to link is not accessible nor instantiable!", e);
		}
	}
	
	public boolean hasCall(String call){
		
		if(call != null && call.length() != 0){
			
			List<String> calls = this.getInstance().getAllCalls();
			
			for(String definedCall : calls){
				if(definedCall.equals(call)){
					return true;
				}
			}
			
		}
		
		return false;
		
	}
	
	public String getCall(){
		try{
			return this.getInstance().getCall();
		}
		catch(IllegalStateException e){
			return null;
		}
	}
	
	public Class<? extends LinkableCommand> getClassToLink(){
		return this.classToLink;
	}
	
}
