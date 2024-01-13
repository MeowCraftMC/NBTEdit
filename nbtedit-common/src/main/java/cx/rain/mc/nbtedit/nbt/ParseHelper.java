package cx.rain.mc.nbtedit.nbt;

public class ParseHelper {

    public static byte parseByte(String s) throws NumberFormatException {
        try {
            return Byte.parseByte(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid byte");
        }
    }

    public static short parseShort(String s) throws NumberFormatException {
        try {
            return Short.parseShort(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid short");
        }
    }

    public static int parseInt(String s) throws NumberFormatException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid int");
        }
    }

    public static long parseLong(String s) throws NumberFormatException {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid long");
        }
    }

    public static float parseFloat(String s) throws NumberFormatException {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid float");
        }
    }

    public static double parseDouble(String s) throws NumberFormatException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid double");
        }
    }

    public static byte[] parseByteArray(String s) throws NumberFormatException {
        try {
            var input = s.split(",");
            var arr = new byte[input.length];
            for (int i = 0; i < input.length; ++i) {
                arr[i] = parseByte(input[i]);
            }
            return arr;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid byte array");
        }
    }

    public static int[] parseIntArray(String s) throws NumberFormatException {
        try {
            var input = s.split(",");
            var arr = new int[input.length];
            for (int i = 0; i < input.length; ++i) {
                arr[i] = parseInt(input[i]);
            }
            return arr;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid int array");
        }
    }

    public static long[] parseLongArray(String s) throws NumberFormatException {
        try {
            var input = s.split(",");
            var arr = new long[input.length];
            for (int i = 0; i < input.length; ++i) {
                arr[i] = parseInt(input[i]);
            }
            return arr;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a valid long array");
        }
    }
}
