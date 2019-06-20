package io.github.vhoyon.vramework.abstracts;

public abstract class AbstractCommandConfirmed extends AbstractBotCommand {
	
	private AbstractBotCommand inceptionCommand = null;
	
	public AbstractCommandConfirmed(){}
	
	public AbstractCommandConfirmed(AbstractBotCommand inceptionCommand){
		super(inceptionCommand);
		
		this.inceptionCommand = inceptionCommand;
		
		action();
	}
	
	public abstract String getConfMessage();
	
	@Override
	public void actions(){
		
		if(!hasMemory(BUFFER_CONFIRMATION)){
			
			this.actionIfConfirmable();
			
			remember(this, BUFFER_CONFIRMATION);
			
		}
		else{
			this.actionIfNotConfirmable();
		}
		
	}
	
	protected abstract void actionIfConfirmable();
	
	protected void actionIfNotConfirmable(){}
	
	public abstract void confirmed();
	
	public abstract void cancelled();
	
	@Override
	public String lang(String shortKey){
		if(inceptionCommand != null){
			return inceptionCommand.lang(shortKey);
		}
		return super.lang(shortKey);
	}
	
	@Override
	public String lang(String shortKey, Object... replacements){
		if(inceptionCommand != null){
			return inceptionCommand.lang(shortKey, replacements);
		}
		return super.lang(shortKey, replacements);
	}
	
	@Override
	public String getCall(){
		if(inceptionCommand == null)
			return null;
		
		return inceptionCommand.getCall();
	}
	
}
