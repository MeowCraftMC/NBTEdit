package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.network.chat.Component;

public enum NbtType {
    BYTE(1, ModConstants.NBT_TYPE_BYTE, "Byte"),
    SHORT(2, ModConstants.NBT_TYPE_SHORT, "Short"),
    INT(3, ModConstants.NBT_TYPE_INT, "Int"),
    LONG(4, ModConstants.NBT_TYPE_LONG, "Long"),
    FLOAT(5, ModConstants.NBT_TYPE_FLOAT, "Float"),
    DOUBLE(6, ModConstants.NBT_TYPE_DOUBLE, "Double"),
    BYTE_ARRAY(7, ModConstants.NBT_TYPE_BYTE_ARRAY, "ByteArray"),
    STRING(8, ModConstants.NBT_TYPE_STRING, "String"),
    LIST(9, ModConstants.NBT_TYPE_LIST, "List"),
    COMPOUND(10, ModConstants.NBT_TYPE_COMPOUND, "Compound"),
    INT_ARRAY(11, ModConstants.NBT_TYPE_INT_ARRAY, "IntArray"),
    LONG_ARRAY(12, ModConstants.NBT_TYPE_LONG_ARRAY, "LongArray"),
    ;

    private final int id;
    private final Component name;
    private final String tagName;

    NbtType(int id, String name, String tagName) {
        this.id = id;
        this.name = Component.translatable(name);
        this.tagName = tagName;
    }

    public int getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public String getTagName() {
        return tagName;
    }

    public static NbtType of(int id) {
        for (var v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }

        return BYTE;
    }

    public static NbtType ofButtonId(int id) {
        return of(id + 1);
    }
}
