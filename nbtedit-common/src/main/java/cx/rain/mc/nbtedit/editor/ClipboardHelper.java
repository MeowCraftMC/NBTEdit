package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.client.Minecraft;

public class ClipboardHelper {
    public static void setNode(NBTTree.Node<?> node) {
        var data = node.asString();
        Minecraft.getInstance().keyboardHandler.setClipboard(data);
    }

    public static NBTTree.Node<?> getNode() {
        var data = Minecraft.getInstance().keyboardHandler.getClipboard();
        return NBTTree.Node.fromString(data);
    }
}
