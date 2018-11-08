package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.exceptions.AmountNotDefinedException;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.utilities.sanitizers.EnumSanitizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LangAmountManager {
	
	public List<LangAmount> langAmounts;
	
	private class LangAmount {
		
		int countMin;
		int countMax;
		String message;
		
		public LangAmount(String messyLine) throws BadFormatException{
			
			String regexAnyNumber = "0|(?:- ?)?[1-9][0-9]*";
			String regexAnyNumberOrStar = regexAnyNumber + "|\\*";
			
			String regexRangeDefiner = "\\[(" + regexAnyNumberOrStar
					+ ")(?:, ?(" + regexAnyNumberOrStar + "))?]";
			
			String regexMessyLineResolver = regexRangeDefiner + "\\s*(.+)";
			
			Matcher matcher = Pattern.compile(regexMessyLineResolver).matcher(
					messyLine);
			
			if(!matcher.matches()){
				throw new BadFormatException(
						"Line \""
								+ messyLine
								+ "\" is not formatted correctly. Make sure to write it using the format \"[#,#] Text\"!",
						3);
			}
			
			String possibleMin = matcher.group(1);
			String possibleMax = matcher.group(2);
			
			try{
				
				this.countMin = (possibleMin.equals("*")) ? Integer.MIN_VALUE
						: Integer.parseInt(possibleMin);
				
				if(possibleMax == null){
					this.countMax = this.countMin;
				}
				else{
					this.countMax = (possibleMax.equals("*")) ? Integer.MAX_VALUE
							: Integer.parseInt(possibleMax);
				}
				
			}
			catch(NumberFormatException e){
				throw new BadFormatException(
						"The range "
								+ possibleMin
								+ " to "
								+ possibleMax
								+ " (given in line \""
								+ messyLine
								+ "\") is outside of the possible range.\nMake sure to use integer values between "
								+ Integer.MIN_VALUE + " and "
								+ Integer.MAX_VALUE + ".", 4);
			}
			
			this.message = matcher.group(3);
			
		}
		
		public LangAmount(int countMin, int countMax, String message){
			this.countMin = countMin;
			this.countMax = countMax;
			this.message = message;
		}
		
		public LangAmount(int countMin, char countMax, String message){
			this(countMin, countMax == '*' ? Integer.MAX_VALUE : Character
					.getNumericValue(countMax), message);
		}
		
		public LangAmount(char countMin, int countMax, String message){
			this(countMin == '*' ? Integer.MIN_VALUE : Character
					.getNumericValue(countMax), countMax, message);
		}
		
		public LangAmount(char countMin, char countMax, String message){
			this(countMin == '*' ? Integer.MIN_VALUE : Character
					.getNumericValue(countMax),
					countMax == '*' ? Integer.MAX_VALUE : Character
							.getNumericValue(countMax), message);
		}
		
		@Override
		public String toString(){
			return "[min: " + countMin + ", max: " + countMax + ", message: \""
					+ message + "\"]";
		}
		
	}
	
	public LangAmountManager(String langLine) throws BadFormatException{
		
		List<String> unparsedMessages = EnumSanitizer
				.extractEnumFromString(langLine);
		
		ArrayList<LangAmount> list = new ArrayList<>();
		
		for(String messyLine : unparsedMessages){
			
			LangAmount langAmount = new LangAmount(messyLine);
			
			if(!langAmountNonUsedRange(langAmount, list)){
				
				throw new BadFormatException(
						"Another message already has at least one number of the range ["
								+ langAmount.countMin + ","
								+ langAmount.countMax
								+ "], please verify your strings!", 5);
				
			}
			
			list.add(langAmount);
			
		}
		
		this.langAmounts = list;
		
	}
	
	public String getMessageAmount(int amount) throws AmountNotDefinedException{
		
		for(LangAmount langAmount : this.langAmounts){
			
			if(amount >= langAmount.countMin && amount <= langAmount.countMax)
				return langAmount.message;
			
		}
		
		throw new AmountNotDefinedException(amount);
		
	}
	
	protected static boolean langAmountNonUsedRange(LangAmount newLangAmount,
			List<LangAmount> langAmountList){
		
		for(LangAmount listedLangAmount : langAmountList){
			
			int maxedMin = Math.max(newLangAmount.countMin,
					listedLangAmount.countMin);
			int minedMax = Math.min(newLangAmount.countMax,
					listedLangAmount.countMax);
			
			if(maxedMin < minedMax)
				return false;
			
		}
		
		return true;
		
	}
	
}
