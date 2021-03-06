package io.github.vhoyon.vramework.abstracts;

public abstract class AbstractTextCommand extends AbstractBotCommand {
	
	protected enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	@Override
	public void actions(){
		
		String textToSend = this.getTextToSend();
		Boolean isInfoOneLiner = this.isTextInfoOneLiner();
		
		TextType textType;
		
		if(isInfoOneLiner == null){
			textType = TextType.SIMPLE;
		}
		else{
			
			textType = isInfoOneLiner ? TextType.INFO_LINE
					: TextType.INFO_BLOCK;
			
		}
		
		this.sendMessageMethod(textToSend, textType);
		
	}
	
	protected abstract void sendMessageMethod(String textToSend,
			TextType textType);
	
	public abstract String getTextToSend();
	
	public Boolean isTextInfoOneLiner(){
		return null;
	}
	
	@Override
	public String getCall(){
		return null;
	}
	
}
