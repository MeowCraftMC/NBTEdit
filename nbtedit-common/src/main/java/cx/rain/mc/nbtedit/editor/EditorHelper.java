package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.nbt.*;

public class EditorHelper {
    public static Tag newTag(int buttonId) {
        return switch (buttonId) {
            case 0 -> ByteTag.valueOf((byte) 0);
            case 1 -> ShortTag.valueOf((short) 0);
            case 2 -> IntTag.valueOf(0);
            case 3 -> LongTag.valueOf(0);
            case 4 -> FloatTag.valueOf(0.0f);
            case 5 -> DoubleTag.valueOf(0.0);
            case 6 -> new ByteArrayTag(new byte[0]);
            case 7 -> StringTag.valueOf("");
            case 8 -> new ListTag();
            case 9 -> new CompoundTag();
            case 10 -> new IntArrayTag(new int[0]);
            case 11 -> new LongArrayTag(new long[0]);
            default -> null;
        };
    }

    public static String newTagName(int buttonId, NBTTree.Node<?> parent) {
        var type = NbtType.ofButtonId(buttonId);
        if (!parent.hasChild()) {
            return type + " 1";
        }

        for (int i = 1; i <= parent.getChildren().size() + 1; ++i) {
            String name = type.getTagName() + " " + i;
            if (isNameValidInNode(name, parent)) {
                return name;
            }
        }

        return type + " INF";
    }

    public static boolean isNameValidInNode(String name, NBTTree.Node<?> parent) {
        for (var node : parent.getChildren()) {
            if (node.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
