package io.github.vhoyon.vramework.utilities.formatting;

public interface DiscordFormatter {
	
	/**
	 * Format the text as bold.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>**</b>.
	 */
	default String bold(Object text){
		
		String stringText = text.toString();
		
		// Don't format if already formatted
		if(stringText.matches("^\\*\\*.*\\*\\*$"))
			return stringText;
		
		return "**" + text.toString().replaceAll("\\*", "\\*") + "**";
		
	}
	
	/**
	 * Format the text as italic.
	 * 
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>_</b>.
	 */
	default String ital(Object text){
		
		String stringText = text.toString();
		
		// Don't format if already formatted
		if(stringText.matches("^_(?!_).*(?<!_)_$"))
			return stringText;
		
		return "_" + text.toString().replaceAll("_", "\\_") + "_";
		
	}
	
	/**
	 * Format the text as underscored.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>__</b>.
	 */
	default String unde(Object text){
		
		String stringText = text.toString();
		
		// Don't format if already formatted
		if(stringText.matches("^__.*__$"))
			return stringText;
		
		return "__" + text.toString().replaceAll("_", "\\_") + "__";
		
	}
	
	/**
	 * Format the text as strikethrough.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>~~</b>.
	 */
	default String strk(Object text){
		
		String stringText = text.toString();
		
		// Don't format if already formatted
		if(stringText.matches("^~~.*~~$"))
			return stringText;
		
		return "~~" + stringText.replaceAll("~", "\\~") + "~~";
		
	}
	
	/**
	 * Format the text as a code line.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>`</b>.
	 */
	default String code(Object text){
		
		String stringText = text.toString();
		
		// Don't format if already formatted
		if(stringText.matches("^(_|\\*\\*)?``.*``(_|\\*\\*)?$"))
			return stringText;
		
		String escapedTicks = stringText.replaceFirst("^``", "\u200B``")
				.replaceFirst("``$", "``\u200B").replaceAll("``", "`\u200B`");
		
		String escapedItalics = escapedTicks.replaceAll(
				"(?<!\\*\\*)_(?<!\\*\\*)(.*)(?<!\\*\\*)_(?<!\\*\\*)",
				"``_``$1``_``");
		
		String escapedBold = escapedItalics.replaceAll(
				"(?<!_)\\*\\*(?<!_)(.*)(?<!_)\\*\\*(?<!_)", "``**``$1``**``");
		
		String escapedBoldItalics = escapedBold.replaceAll(
				"(?:\\*\\*_)(.*)(?:_\\*\\*)", "``**_``$1``_**``");
		
		String escapedItalicsBold = escapedBoldItalics.replaceAll(
				"(?:_\\*\\*)(.*)(?:\\*\\*_)", "``_**``$1``**_``");
		
		String cleanedStart = escapedItalicsBold.replaceFirst("^`+(_|\\*\\*)",
				"$1");
		String cleanedEnd = cleanedStart.replaceFirst("(_|\\*\\*)`+$", "$1");
		
		String codified = ((cleanedEnd.matches("^(_|\\*\\*).*")) ? "" : "``")
				+ cleanedEnd
				+ ((cleanedEnd.matches(".*(_|\\*\\*)$")) ? "" : "``");
		
		return codified;
		
	}
	
	/**
	 * Format the text as a block of code (with no specified language).
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>```</b> without any language specified.
	 */
	default String bloc(Object text){
		return bloc(text, "");
	}
	
	/**
	 * Format the text as a block of code for a specified language.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>```</b> with the language specified in the parameter
	 *         <i>lang</i>.
	 */
	default String bloc(Object text, String lang){
		return "```" + lang + "\n"
				+ text.toString().replaceAll("```", "`\u200B`\u200B`")
				+ "\n```";
	}
	
}
