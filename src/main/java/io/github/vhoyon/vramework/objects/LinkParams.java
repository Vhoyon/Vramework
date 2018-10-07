package io.github.vhoyon.vramework.objects;

import java.util.ArrayList;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;

public class LinkParams extends Link {
	
	private Object[] params;
	
	public LinkParams(Link link, Object... params){
		super(link.getClassToLink());
		
		this.params = params;
	}
	
	@Override
	public LinkableCommand getInstance() throws Exception{
		
		@SuppressWarnings("rawtypes")
		ArrayList<Class> classes = new ArrayList<>();
		
		for(Object object : params){
			classes.add(object.getClass());
		}
		
		return getClassToLink().getDeclaredConstructor(
				(Class[])classes.toArray(new Class[classes.size()]))
				.newInstance(params);
		
	}
	
}
