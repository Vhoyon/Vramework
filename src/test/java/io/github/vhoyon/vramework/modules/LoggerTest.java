package io.github.vhoyon.vramework.modules;

import io.github.vhoyon.vramework.interfaces.Loggable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class LoggerTest {
	
	@Mock
	Loggable mockLoggable;
	
	@BeforeEach
	void setUp(){
		mockLoggable = mock(Loggable.class);
		
		Logger.setOutputs(mockLoggable);
	}
	
	@AfterEach
	void setDown(){
		mockLoggable = null;
	}
	
	@Test
	void testErrorLogContainsContext(){
		
		doAnswer(
				(i) -> {
					
					assertTrue(((String)i.getArgument(0))
							.contains("Error in LoggerTest"));
					
					return null;
				}).when(mockLoggable).log("Testing stuff", "ERROR", true);
		
		Logger.log("Testing stuff", Logger.LogType.ERROR);
		
	}
	
}
