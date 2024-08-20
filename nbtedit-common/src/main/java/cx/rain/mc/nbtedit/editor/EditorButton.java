package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.network.chat.Component;

public enum EditorButton {
    BYTE(0, ModConstants.NBT_TYPE_BYTE),
    SHORT(1, ModConstants.NBT_TYPE_SHORT),
    INT(2, ModConstants.NBT_TYPE_INT),
    LONG(3, ModConstants.NBT_TYPE_LONG),
    FLOAT(4, ModConstants.NBT_TYPE_FLOAT),
    DOUBLE(5, ModConstants.NBT_TYPE_DOUBLE),
    BYTE_ARRAY(6, ModConstants.NBT_TYPE_BYTE_ARRAY),
    STRING(7, ModConstants.NBT_TYPE_STRING),
    LIST(8, ModConstants.NBT_TYPE_LIST),
    COMPOUND(9, ModConstants.NBT_TYPE_COMPOUND),
    INT_ARRAY(10, ModConstants.NBT_TYPE_INT_ARRAY),
    LONG_ARRAY(11, ModConstants.NBT_TYPE_LONG_ARRAY),
    EDIT(12, ModConstants.GUI_BUTTON_EDIT),
    DELETE(13, ModConstants.GUI_BUTTON_DELETE),
    PASTE(14, ModConstants.GUI_BUTTON_PASTE),
    CUT(15, ModConstants.GUI_BUTTON_CUT),
    COPY(16, ModConstants.GUI_BUTTON_COPY),
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
