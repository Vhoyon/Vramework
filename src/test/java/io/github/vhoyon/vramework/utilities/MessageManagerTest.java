package io.github.vhoyon.vramework.utilities;

import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.objects.Dictionary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

public class MessageManagerTest implements Utils {
	
	@Mock
	Dictionary mockDict;
	
	String[] langs =
	{
		"Test1Lang", "Test2Lang", "Test3Lang", "Test4Lang", "Test5Lang",
	};
	
	String[] langsRepl =
	{
		"Test1Lang {1}",
		"Test2Lang {1}",
		"Test3Lang {1}",
		"Test4Lang {1}",
		"Test5Lang {1}",
	};
	
	@BeforeEach
	void setUp(){
		mockDict = Mockito.spy(Dictionary.class);
		
		doReturn(langs[0]).when(mockDict).getString("test1", null);
		doReturn(langs[1]).when(mockDict).getString("test2", null);
		doReturn(langs[2]).when(mockDict).getString("test3", null);
		doReturn(langs[3]).when(mockDict).getString("test4", null);
		doReturn(langs[4]).when(mockDict).getString("test5", null);
		
		doReturn(langsRepl[0]).when(mockDict).getString("test1repl", null);
		doReturn(langsRepl[1]).when(mockDict).getString("test2repl", null);
		doReturn(langsRepl[2]).when(mockDict).getString("test3repl", null);
		doReturn(langsRepl[3]).when(mockDict).getString("test4repl", null);
		doReturn(langsRepl[4]).when(mockDict).getString("test5repl", null);
	}
	
	@AfterEach
	void tearDown(){
		mockDict = null;
	}
	
	@Test
	void testGetMessage(){
		
		MessageManager manager = new MessageManager();
		
		manager.addMessage(1, "test1");
		manager.addMessage(2, "test2");
		manager.addMessage(3, "test3");
		
		assertEquals(langs[0], manager.getMessage(1, mockDict));
		assertEquals(langs[1], manager.getMessage(2, mockDict));
		assertEquals(langs[2], manager.getMessage(3, mockDict));
		
	}
	
	@Test
	void testGetMessageReplacements(){
		
		MessageManager manager = new MessageManager();
		
		manager.addMessage(1, "test1repl", "replacement");
		manager.addMessage(2, "test2repl", "replacement");
		manager.addMessage(3, "test3repl", "replacement");
		
		manager.addReplacement("replacement", "hi");
		
		String formattedLang1 = format(langsRepl[0], "hi");
		String formattedLang2 = format(langsRepl[1], "hi");
		String formattedLang3 = format(langsRepl[2], "hi");
		
		assertEquals(formattedLang1, manager.getMessage(1, mockDict));
		assertEquals(formattedLang2, manager.getMessage(2, mockDict));
		assertEquals(formattedLang3, manager.getMessage(3, mockDict));
		
	}
	
	@Test
	void testGetMessageRaw(){
		
		MessageManager manager = new MessageManager();
		
		manager.addMessage(1, "test1");
		manager.addMessage(2, "test2");
		manager.addMessage(3, "test3");
		
		MessageManager.Message message = manager.getMessageRaw(1);
		
		assertEquals("test1", message.langKey);
		assertEquals(0, message.replacementsKeys.length);
		
	}
	
}
