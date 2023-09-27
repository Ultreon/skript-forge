package ch.njol.skript.util;

import org.jetbrains.annotations.NotNull;

public class ChatFormattingUtil {
	public static final char COLOR_CHAR = '§';

	@NotNull
	public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = COLOR_CHAR;
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
	}
}
