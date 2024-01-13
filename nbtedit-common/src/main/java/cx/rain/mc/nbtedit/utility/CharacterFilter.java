package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.nbt.NBTHelper;
import net.minecraft.SharedConstants;

public class CharacterFilter {
	public static String filerAllowedCharacters(String str, boolean section) {
		StringBuilder sb = new StringBuilder();
		char[] arr = str.toCharArray();
		for (char c : arr) {
			if (SharedConstants.isAllowedChatCharacter(c) || (section && (c == NBTHelper.SECTION_SIGN || c == '\n')))
				sb.append(c);
		}

		return sb.toString();
	}
}
