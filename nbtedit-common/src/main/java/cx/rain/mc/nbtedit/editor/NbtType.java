package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum NbtType {
    BYTE(1, Constants.NBT_TYPE_BYTE, "Byte"),
    SHORT(2, Constants.NBT_TYPE_SHORT, "Short"),
    INT(3, Constants.NBT_TYPE_INT, "Int"),
    LONG(4, Constants.NBT_TYPE_LONG, "Long"),
    FLOAT(5, Constants.NBT_TYPE_FLOAT, "Float"),
    DOUBLE(6, Constants.NBT_TYPE_DOUBLE, "Double"),
    BYTE_ARRAY(7, Constants.NBT_TYPE_BYTE_ARRAY, "ByteArray"),
    STRING(8, Constants.NBT_TYPE_STRING, "String"),
    LIST(9, Constants.NBT_TYPE_LIST, "List"),
    COMPOUND(10, Constants.NBT_TYPE_COMPOUND, "Compound"),
    INT_ARRAY(11, Constants.NBT_TYPE_INT_ARRAY, "IntArray"),
    LONG_ARRAY(12, Constants.NBT_TYPE_LONG_ARRAY, "LongArray"),
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
