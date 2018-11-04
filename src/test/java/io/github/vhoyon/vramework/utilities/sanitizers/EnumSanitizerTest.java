package io.github.vhoyon.vramework.utilities.sanitizers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import io.github.vhoyon.vramework.exceptions.BadFormatException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumSanitizerTest {
	
	@Test
	void testSingleValueNoSpace(){
		
		String value = "Hello!";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello!", list.get(0));
		
	}
	
	@Test
	void testSingleValueSpaces(){
		
		String value = "Hello fellow kid!";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello fellow kid!", list.get(0));
		
	}
	
	@Test
	void testSingleValueTabs(){
		
		String value = "Hello\tfellow\tkid!";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello\tfellow\tkid!", list.get(0));
		
	}
	
	@Test
	void testSingleValueSpacesAndTabs(){
		
		String value = "Hello\t fellow \t kid!";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello\t fellow \t kid!", list.get(0));
		
	}
	
	@Test
	void testSingleValueTrailingSpaces(){
		
		String value = "  Hello!  ";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello!", list.get(0));
		
	}
	
	@Test
	void testSingleValueTrailingTabs(){
		
		String value = "\tHello!\t";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello!", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnly(){
		
		String value = "|";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("|", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnlyProtected(){
		
		String value = "\\|";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("\\|", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnlyMultiple(){
		
		String value = "||||";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("||||", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnlyCustom(){
		
		String value = "[";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(1, list.size());
		assertEquals("[", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnlyProtectedCustom(){
		
		String value = "\\[";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(1, list.size());
		assertEquals("\\[", list.get(0));
		
	}
	
	@Test
	void testSingleValueSeparatorOnlyMultipleCustom(){
		
		String value = "[[[[";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(1, list.size());
		assertEquals("[[[[", list.get(0));
		
	}
	
	@Test
	void testSingleValueProtectionSyntax(){
		
		String value = "\\\\||\\";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("\\|", list.get(0));
		assertEquals("\\", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectionSyntaxCustom(){
		
		String value = "\\\\[[\\";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("\\[", list.get(0));
		assertEquals("\\", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectionSyntaxProtector(){
		
		String value = "\\\\\\ \\ \\\\";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(2, list.size());
		assertEquals("\\\\", list.get(0));
		assertEquals("\\", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectionSyntax2(){
		
		String value = "\\\\| | \\|";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("\\|", list.get(0));
		assertEquals("|", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesNoSpace(){
		
		String value = "First|Second|Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpaces(){
		
		String value = "First  |  Second  |  Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesTabs(){
		
		String value = "First\t|\tSecond\t|\tThird";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpacesAndTabs(){
		
		String value = "First\t  | \t Second \t  |\t  Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testSingleValueProtectedBar(){
		
		String value = "Hello \\| World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello | World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedBar(){
		
		String value = "First \\| Second | Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("First | Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarOnlyStart(){
		
		String value = "\\| | Second";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("|", list.get(0));
		assertEquals("Second", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarOnlyEnd(){
		
		String value = "First | \\|";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("|", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedBar(){
		
		String value = "Hello \\\\| World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello \\| World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedProtectedBar(){
		
		String value = "First \\\\| Second | Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("First \\| Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testRegexCharInValue(){
		
		String value = "First | Se[ond";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Se[ond", list.get(1));
		
	}
	
	@Test
	void testSeparatorRegexChar(){
		
		String value = "First [ Second";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		
	}
	
	@Test
	void testSeparatorRegexCharAndRegexInValue(){
		
		String value = "First [ Secon]";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Secon]", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectedBarRegexSeparator(){
		
		String value = "Hello \\[ World";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(1, list.size());
		assertEquals("Hello [ World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarRegexSeparator(){
		
		String value = "First \\[ Second [ Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("First [ Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarRegexSeparatorOnlyStart(){
		
		String value = "\\[ [ Second";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("[", list.get(0));
		assertEquals("Second", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarRegexSeparatorOnlyEnd(){
		
		String value = "First [ \\[";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("[", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedBarRegexSeparator(){
		
		String value = "Hello \\\\[ World";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\[ World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedProtectedBarRegexSeparator(){
		
		String value = "First \\\\[ Second [ Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertEquals(2, list.size());
		assertEquals("First \\[ Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesNoSpaceLetter(){
		
		String value = "FirstlSecondlThird";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpacesLetter(){
		
		String value = "First  l  Second  l  Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesTabsLetter(){
		
		String value = "First\tl\tSecond\tl\tThird";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpacesAndTabsLetter(){
		
		String value = "First\t  l \t Second \t  l\t  Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testSingleValueProtectedBarLetter(){
		
		String value = "He\\l\\lo \\l Wor\\ld";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(1, list.size());
		assertEquals("Hello l World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarLetter(){
		
		String value = "First \\l Second l Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(2, list.size());
		assertEquals("First l Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarOnlyStartLetter(){
		
		String value = "\\l l Second";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(2, list.size());
		assertEquals("l", list.get(0));
		assertEquals("Second", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedBarOnlyEndLetter(){
		
		String value = "First l \\l";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("l", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedBarLetter(){
		
		String value = "He\\l\\lo \\\\l Wor\\ld";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\l World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedProtectedBarLetter(){
		
		String value = "First \\\\l Second l Third";
		
		ArrayList<String> list = EnumSanitizer
				.extractEnumFromString(value, 'l');
		
		assertEquals(2, list.size());
		assertEquals("First \\l Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesNoSpaceProtectorSeparator(){
		
		String value = "First\\Second\\Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpacesProtectorSeparator(){
		
		String value = "First  \\  Second  \\  Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesTabsProtectorSeparator(){
		
		String value = "First\t\\\tSecond\t\\\tThird";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testMultipleValuesSpacesAndTabsProtectorSeparator(){
		
		String value = "First\t  \\ \t Second \t  \\\t  Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(3, list.size());
		assertEquals("First", list.get(0));
		assertEquals("Second", list.get(1));
		assertEquals("Third", list.get(2));
		
	}
	
	@Test
	void testSingleValueProtectedProtectorSeparator(){
		
		String value = "Hello \\\\ World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\ World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedProtectorSeparator(){
		
		String value = "First \\\\ Second \\ Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(2, list.size());
		assertEquals("First \\ Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedOnlyStartProtectorSeparator(){
		
		String value = "\\\\ \\ Second";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(2, list.size());
		assertEquals("\\", list.get(0));
		assertEquals("Second", list.get(1));
		
	}
	
	@Test
	void testMultipleValuesProtectedOnlyEndProtectorSeparator(){
		
		String value = "First \\ \\\\";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(2, list.size());
		assertEquals("First", list.get(0));
		assertEquals("\\", list.get(1));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedProtectorSeparator(){
		
		String value = "Hello \\\\\\ World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\\\ World", list.get(0));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedProtectorSeparatorBig(){
		
		String value = "Hello \\\\\\\\\\\\\\ World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\\\\\\\\\\\ World", list.get(0));
		
	}
	
	@Test
	void testSingleValueProtectedTwice(){
		
		String value = "Hello \\| \\| World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value);
		
		assertEquals(1, list.size());
		assertEquals("Hello | | World", list.get(0));
		
	}
	
	@Test
	void testSingleValueProtectedProtectedProtectorSeparatorTwice(){
		
		String value = "Hello \\\\ \\\\ World";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(1, list.size());
		assertEquals("Hello \\ \\ World", list.get(0));
		
	}
	
	@Test
	void testMultipleValuesProtectedProtectedBarProtectorSeparator(){
		
		String value = "First \\\\\\ Second \\ Third";
		
		ArrayList<String> list = EnumSanitizer.extractEnumFromString(value,
				'\\');
		
		assertEquals(2, list.size());
		assertEquals("First \\\\ Second", list.get(0));
		assertEquals("Third", list.get(1));
		
	}
	
	@Test
	void testEmptyValue(){
		
		String value = "";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat1(){
		
		String value = "First|";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat2(){
		
		String value = "First||Third";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat3(){
		
		String value = "First|  |Third";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat4(){
		
		String value = "First|Second|";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat5(){
		
		String value = "|First|Second";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat6(){
		
		String value = "    |First";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormat7(){
		
		String value = "First|    ";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value);
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep1(){
		
		String value = "First[";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep2(){
		
		String value = "First[[Third";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep3(){
		
		String value = "First[  [Third";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep4(){
		
		String value = "First[Second[";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep5(){
		
		String value = "[First[Second";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep6(){
		
		String value = "    [First";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
	@Test
	void testMultipleValuesBadFormatCustomSep7(){
		
		String value = "First[    ";
		
		Executable shouldThrowBadFormatException = () -> EnumSanitizer
				.extractEnumFromString(value, '[');
		
		assertThrows(BadFormatException.class, shouldThrowBadFormatException);
		
	}
	
}