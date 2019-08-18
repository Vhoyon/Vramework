package io.github.vhoyon.vramework.interfaces;

public enum Emoji {
	
	// Faces
	FACE_SWEAT_SMILE("sweat_smile", "\uD83D\uDE05"),
	FACE_JOY("joy", "\uD83D\uDE02"),
	FACE_INNOCENT("innocent", ""),
	FACE_WEARY("weary", "\uD83D\uDE29"),
	FACE_SMILEY("smiley", "\uD83D\uDE03"),
	FACE_SMILE("smile", "\uD83D\uDE04"),
	FACE_TIRED("tired_face", "\uD83D\uDE2B"),
	FACE_LAUGHING("laughing", "\uD83D\uDE06"),
	FACE_WINK("wink", "\uD83D\uDE09"),
	FACE_BLUSH("blush", "\uD83D\uDE0A"),
	FACE_UPSIDE_DOWN("upside_down", "\uD83D\uDE43"),
	FACE_RELAXED("relaxed", "\u263A"),
	FACE_YUM("yum", "\uD83D\uDE0B"),
	FACE_RELIEVED("relieved", "\uD83D\uDE0C"),
	FACE_HEART_EYES("heart_eyes", "\uD83D\uDE0D"),
	FACE_KISSING_HEART("kissing_heart", "\uD83D\uDE18"),
	FACE_KISSING("kissing", "\uD83D\uDE17"),
	FACE_SMIRK("smirk", "\uD83D\uDE0F"),
	FACE_NO_MOUTH("no_mouth", "\uD83D\uDE36"),
	FACE_NEUTRAL("neutral_face", "\uD83D\uDE10"),
	FACE_EXPRESSIONLESS("expressionless", "\uD83D\uDE11"),
	FACE_UNAMUSED("unamused", "\uD83D\uDE12"),
	FACE_ROLLING_EYES("rolling_eyes", "\uD83D\uDE44"),
	FACE_THINKING("thinking", "\uD83E\uDD14"),
	FACE_SLIGHT_SMILE("slight_smile", "\uD83D\uDE42"),
	FACE_KISSING_SMILING_EYES("kissing_smiling_eyes", "\uD83D\uDE19"),
	FACE_KISSING_CLOSED_EYES("kissing_closed_eyes", "\uD83D\uDE1A"),
	FACE_STUCK_OUT_TONGUE_WINKING("stuck_out_tongue_winking_eye", "\uD83D\uDE1C"),
	FACE_STUCK_OUT_TONGUE_CLOSED_EYES("stuck_out_tongue_closed_eyes", "\uD83D\uDE1D"),
	FACE_STUCK_OUT_TONGUE("stuck_out_tongue", "\uD83D\uDE1B"),
	FACE_SUNGLASSES("sunglasses", "\uD83D\uDE0E"),
	FACE_HUGGING("hugging", "\uD83E\uDD17"),
	FACE_MONEY_MOUTH("money_mouth", "\uD83E\uDD11"),
	FACE_NERD("nerd", "\uD83E\uDD13"),
	
	// Numbers
	NUMBER_0("zero", "\u0030\u20E3"),
	NUMBER_1("one", "\u0031\u20E3	"),
	NUMBER_2("two", "\u0032\u20E3"),
	NUMBER_3("three", "\u0033\u20E3"),
	NUMBER_4("four", "\u0034\u20E3"),
	NUMBER_5("five", "\u0035\u20E3"),
	NUMBER_6("six", "\u0036\u20E3"),
	NUMBER_7("seven", "\u0037\u20E3"),
	NUMBER_8("eight", "\u0038\u20E3"),
	NUMBER_9("nine", "\u0039\u20E3"),
	NUMBER_10("keycap_ten", "\uD83D\uDD1F"),
	
	ONE_HUNDRED("100", "\uD83D\uDCAF"),
	
	// Misc
	
	CHECKMARK("heavy_check_mark", "\u2714"),
	CHECKMARK_BOX("white_check_mark", "\u2705"),
	RED_CROSS("x", "\u274C"),
	
	OK_HAND("ok_hand", "\uD83D\uDC4C"),
	HAMMER_PICK("hammer_pick", "\uD83D\uDEE0"),
	WIP(HAMMER_PICK),
	
	EGGPLANT("eggplant", "\uD83C\uDF46"),
	DICK(EGGPLANT),
	POOP("poop", "\uD83D\uDCA9"),
	
	HEART("heart", "\u2764"),
	
	STOPWATCH("stopwatch", "\u23F1"),
	ALARM_CLOCK("alarm_clock", "\u23F0"),
	
	;
	
	Emoji(String content, String unicodeValue){
		this.content = content;
		this.unicodeValue = unicodeValue;
	}
	
	Emoji(Emoji emoji){
		this(emoji.content, emoji.unicodeValue);
	}
	
	private String content;
	private String unicodeValue;
	
	public String getUnicodeValue(){
		return this.unicodeValue;
	}
	
	@Override
	public String toString() {
		return ":" + this.content + ":";
	}
	
}
