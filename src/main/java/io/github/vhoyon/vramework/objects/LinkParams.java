package io.github.vhoyon.vramework.objects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;

public class LinkParams extends Link {
	
	private Object[] params;
	
	public LinkParams(Link link, Object... params){
		super(link.getClassToLink());
		
		this.params = params;
	}
	
	@Override
	public LinkableCommand getInstance() throws IllegalStateException{
		
		List<Class<?>> classes = new ArrayList<>();
		
		for(Object object : params){
			classes.add(object.getClass());
		}
		
		try{
			return getClassToLink().getDeclaredConstructor(
					(Class<?>[])classes.toArray(new Class<?>[0])).newInstance(
					params);
		}
		catch(InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e){
			throw new IllegalStateException(
					"The command to link is not accessible nor instantiable!");
		}
		
	}
	
}
