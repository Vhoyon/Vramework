package io.github.vhoyon.utilities.formatting;

public interface DiscordFormatter {
	
	/**
	 * Format the text as a code line.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>`</b>.
	 */
	default String code(Object text){
		return "`" + text.toString() + "`";
	}
	
	/**
	 * Format the text as bold.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>**</b>.
	 */
	default String bold(Object text){
		return "**" + text.toString() + "**";
	}
	
	/**
	 * Format the text as italic.
	 * 
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>_</b>.
	 */
	default String ital(Object text){
		return "_" + text.toString() + "_";
	}
	
	/**
	 * Format the text as underscored.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>__</b>.
	 */
	default String unde(Object text){
		return "__" + text.toString() + "__";
	}
	
	/**
	 * Format the text as strikethrough.
	 *
	 * @return An Object that contains the <i>text</i> parameter enclosed with
	 *         <b>~~</b>.
	 */
	default String strk(Object text){
		return "~~" + text.toString() + "~~";
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
		return "```" + lang + "\n" + text.toString() + "\n```";
	}
	
}
