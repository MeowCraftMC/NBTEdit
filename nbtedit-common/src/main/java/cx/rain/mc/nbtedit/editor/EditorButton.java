package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum EditorButton {
    BYTE(0, Constants.NBT_TYPE_BYTE),
    SHORT(1, Constants.NBT_TYPE_SHORT),
    INT(2, Constants.NBT_TYPE_INT),
    LONG(3, Constants.NBT_TYPE_LONG),
    FLOAT(4, Constants.NBT_TYPE_FLOAT),
    DOUBLE(5, Constants.NBT_TYPE_DOUBLE),
    BYTE_ARRAY(6, Constants.NBT_TYPE_BYTE_ARRAY),
    STRING(7, Constants.NBT_TYPE_STRING),
    LIST(8, Constants.NBT_TYPE_LIST),
    COMPOUND(9, Constants.NBT_TYPE_COMPOUND),
    INT_ARRAY(10, Constants.NBT_TYPE_INT_ARRAY),
    LONG_ARRAY(11, Constants.NBT_TYPE_LONG_ARRAY),
    EDIT(12, Constants.GUI_BUTTON_EDIT),
    DELETE(13, Constants.GUI_BUTTON_DELETE),
    PASTE(14, Constants.GUI_BUTTON_PASTE),
    CUT(15, Constants.GUI_BUTTON_CUT),
    COPY(16, Constants.GUI_BUTTON_COPY),
    ;

    private final int id;
    private final Component name;

    EditorButton(int id, String name) {
        this.id = id;
        this.name = Component.translatable(name);
    }

    public int getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public static EditorButton of(int id) {
        for (var v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }

        return BYTE;
    }
}
