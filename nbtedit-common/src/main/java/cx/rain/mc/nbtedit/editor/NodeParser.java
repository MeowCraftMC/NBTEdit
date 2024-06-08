package cx.rain.mc.nbtedit.editor;

import net.minecraft.nbt.*;

import java.util.Locale;

/**
 * Parse string with silent exception.
 */
public class NodeParser {

    public static String getString(NbtTree.Node<?> node) {
        var tag = node.getTag();

        if (tag instanceof ByteTag b) {
            return Byte.toString(b.getAsByte());
        }

        if (tag instanceof ShortTag s) {
            return Short.toString(s.getAsShort());
        }

        if (tag instanceof IntTag i) {
            return Integer.toString(i.getAsInt());
        }

        if (tag instanceof LongTag l) {
            return Long.toString(l.getAsLong());
        }

        if (tag instanceof FloatTag f) {
            return Float.toString(f.getAsFloat());
        }

        if (tag instanceof DoubleTag d) {
            return Double.toString(d.getAsDouble());
        }

        if (tag instanceof ByteArrayTag ba) {
            var s = new StringBuilder();
            for (var b : ba.getAsByteArray()) {
                s.append(b).append(", ");
            }
            return s.toString();
        }

        if (tag instanceof IntArrayTag ia) {
            var s = new StringBuilder();
            for (var i : ia.getAsIntArray()) {
                s.append(i).append(", ");
            }
            return s.toString();
        }

        if (tag instanceof LongArrayTag la) {
            var s = new StringBuilder();
            for (var l : la.getAsLongArray()) {
                s.append(l).append(", ");
            }
            return s.toString();
        }

        // List or Compound, returns empty.
        return "";
    }

    public static <T extends Tag> Tag getTag(NbtTree.Node<T> node, String value) {
        Tag tag = node.getTag();
        try {
            if (tag instanceof ByteTag) {
                return ByteTag.valueOf(parseByte(value));
            }
            if (tag instanceof ShortTag) {
                return ShortTag.valueOf(parseShort(value));
            }
            if (tag instanceof IntTag) {
                return IntTag.valueOf(parseInt(value));
            }
            if (tag instanceof LongTag) {
                return LongTag.valueOf(parseLong(value));
            }
            if (tag instanceof FloatTag) {
                return FloatTag.valueOf(parseFloat(value));
            }
            if (tag instanceof DoubleTag) {
                return DoubleTag.valueOf(parseDouble(value));
            }
            if (tag instanceof ByteArrayTag) {
                return new ByteArrayTag(parseByteArray(value));
            }
            if (tag instanceof IntArrayTag) {
                return new IntArrayTag(parseIntArray(value));
            }
            if (tag instanceof LongArrayTag) {
                return new LongArrayTag(parseLongArray(value));
            }
            if (tag instanceof StringTag) {
                return StringTag.valueOf(value);
            }
        } catch (Exception ignored) {
        }

        return EndTag.INSTANCE;
    }

    public static byte parseByte(String s) {
        try {
            var c = s.toLowerCase(Locale.ROOT);
            if (c.endsWith("b")) {
                c = c.substring(0, c.length() - 1);
            }
            return Byte.parseByte(c);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static short parseShort(String s) {
        try {
            var c = s.toLowerCase(Locale.ROOT);
            if (c.endsWith("s")) {
                c = c.substring(0, c.length() - 1);
            }
            return Short.parseShort(c);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String s) {
        try {
            var c = s.toLowerCase(Locale.ROOT);
            if (c.endsWith("l")) {
                c = c.substring(0, c.length() - 1);
            }
            return Long.parseLong(c);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static float parseFloat(String s) {
        try {
            var c = s.toLowerCase(Locale.ROOT);
            if (c.endsWith("f")) {
                c = c.substring(0, c.length() - 1);
            }
            return Float.parseFloat(c);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(String s) {
        try {
            var c = s.toLowerCase(Locale.ROOT);
            if (c.endsWith("d")) {
                c = c.substring(0, c.length() - 1);
            }
            return Double.parseDouble(c);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static byte[] parseByteArray(String s) {
        try {
            var input = s.split(",");
            var arr = new byte[input.length];
            for (int i = 0; i < input.length; ++i) {
                var c = input[i].toLowerCase(Locale.ROOT).strip();
                if (c.endsWith("b")) {
                    c = c.substring(0, c.length() - 1);
                }

                arr[i] = parseByte(c);
            }
            return arr;
        } catch (NumberFormatException e) {
            return new byte[] {0};
        }
    }

    public static int[] parseIntArray(String s) {
        try {
            var input = s.split(",");
            var arr = new int[input.length];
            for (int i = 0; i < input.length; ++i) {
                var c = input[i].strip();
                arr[i] = parseInt(c);
            }
            return arr;
        } catch (NumberFormatException e) {
            return new int[] {0};
        }
    }

    public static long[] parseLongArray(String s) throws NumberFormatException {
        try {
            var input = s.split(",");
            var arr = new long[input.length];
            for (int i = 0; i < input.length; ++i) {
                var c = input[i].toLowerCase(Locale.ROOT).strip();
                if (c.endsWith("l")) {
                    c = c.substring(0, c.length() - 1);
                }
                arr[i] = parseInt(c);
            }
            return arr;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid long array");
        }
    }
}
