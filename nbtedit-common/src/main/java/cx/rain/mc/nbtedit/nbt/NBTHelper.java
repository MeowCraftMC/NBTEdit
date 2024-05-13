package cx.rain.mc.nbtedit.nbt;

import com.google.common.base.Strings;
import net.minecraft.nbt.*;

public class NBTHelper {
	public static final char SECTION_SIGN = '\u00A7';

	public static String getNBTNameSpecial(NBTTree.Node<?> nbtNode) {
		String name = nbtNode.getName();
		Tag tag = nbtNode.getTag();

		if (tag instanceof CompoundTag) {
			return (Strings.isNullOrEmpty(name) ? "(CompoundTag)" : name) + ": ";
		}

		if (tag instanceof ListTag) {
			return (Strings.isNullOrEmpty(name) ? "(ListTag)" : name) + ": ";
		}

		String s = tag.getAsString();
		return Strings.isNullOrEmpty(name) ? s : name + ": " + s + SECTION_SIGN + 'r';
	}

	public static String toString(Tag tag) {
		return tag.getAsString();
	}

	public static Tag newTag(byte type) {
		switch (type) {
			case 0: return EndTag.INSTANCE;
			case 1: return ByteTag.valueOf((byte) 0);
			case 2: return ShortTag.valueOf((short) 0);
			case 3: return IntTag.valueOf(0);
			case 4: return LongTag.valueOf(0);
			case 5: return FloatTag.valueOf(0.0f);
			case 6: return DoubleTag.valueOf(0.0);
			case 7: return new ByteArrayTag(new byte[0]);
			case 8: return StringTag.valueOf("");
			case 9: return new ListTag();
			case 10: return new CompoundTag();
			case 11: return new IntArrayTag(new int[0]);
			case 12: return new LongArrayTag(new long[0]);
			default: return null;
		}
	}

	public static Tag of(byte type, int value) {
		switch (type) {
			case 0: return EndTag.INSTANCE;
			case 1: return ByteTag.valueOf((byte) value);
			case 2: return ShortTag.valueOf((short) value);
			case 3: return IntTag.valueOf(value);
			case 4: return LongTag.valueOf(value);
			case 5: return FloatTag.valueOf(value);
			case 6: return DoubleTag.valueOf(value);
			case 7: return new ByteArrayTag(new byte[0]);
			case 8: return StringTag.valueOf("");
			case 9: return new ListTag();
			case 10: return new CompoundTag();
			case 11: return new IntArrayTag(new int[0]);
			case 12: return new LongArrayTag(new long[0]);
			default: return null;
		}
	}

	public static String getNameByButton(byte id) {
		switch (id) {
			case 1: return "Byte";
			case 2: return "Short";
			case 3: return "Int";
			case 4: return "Long";
			case 5: return "Float";
			case 6: return "Double";
			case 7: return "ByteArray";
			case 8: return "String";
			case 9: return "List";
			case 10: return "Compound";
			case 11: return "IntArray";
			case 12: return "LongArray";
			case 13: return "Edit";
			case 14: return "Delete";
			case 15: return "Paste";
			case 16: return "Cut";
			case 17: return "Copy";
			default: return "Unknown";
		}
	}
}
