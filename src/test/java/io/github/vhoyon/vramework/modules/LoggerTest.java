package io.github.vhoyon.vramework.modules;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import io.github.vhoyon.vramework.interfaces.Loggable;

public class LoggerTest {
	
	@Mock
	Loggable mockLoggable;
	
	@BeforeEach
	void setUp(){
		new Logger().build();
		
		mockLoggable = mock(Loggable.class);
		
		Logger.setOutputs(mockLoggable);
	}
	
	@AfterEach
	void setDown(){
		mockLoggable = null;
	}
	
	@Test
	void testErrorLogContainsContext(){
		
		AtomicReference<String> logMessageRef = new AtomicReference<>();
		
		doAnswer((i) -> {
			
			logMessageRef.set((String)i.getArgument(0));
			
			return null;
		}).when(mockLoggable).log(anyString(), anyString(), anyBoolean());
		
		Logger.log("Testing stuff", Logger.LogType.ERROR, true);
		
		String logMessage = logMessageRef.get();
		
		assertNotNull(logMessage);
		
		String expectedContainClass = "io.github.vhoyon.vramework.modules.LoggerTest";
		
		boolean doesContainClass = logMessage.contains(expectedContainClass);
		
		assertTrue(
				doesContainClass,
				createExpectedMessage("Log message", expectedContainClass,
						logMessage));
		
		// WARNING : If this test class changes, make sure to verify if this line is still valid!
		String expectedContainLineNum = "line 50";
		
		boolean doesContainLineNum = logMessage
				.contains(expectedContainLineNum);
		
		assertTrue(
				doesContainLineNum,
				createExpectedMessage("Log message", expectedContainLineNum,
						logMessage));
		
		String expectedContainMethod = "testErrorLogContainsContext";
		
		boolean doesContainMethod = logMessage.contains(expectedContainMethod);
		
		assertTrue(
				doesContainMethod,
				createExpectedMessage("Log message", expectedContainMethod,
						logMessage));
		
	}
	
	private static String createExpectedMessage(String element,
			String expected, String actual){
		return element
				+ " does not contain the expected data.\nExpected contains\t\t\t: "
				+ expected + "\nActual text (between lines)\t:\n-----\n"
				+ actual + "\n-----";
	}
}
