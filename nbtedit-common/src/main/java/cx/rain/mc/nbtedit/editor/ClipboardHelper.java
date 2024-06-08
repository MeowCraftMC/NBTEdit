package cx.rain.mc.nbtedit.editor;

import net.minecraft.client.Minecraft;

public class ClipboardHelper {
    public static void setNode(NbtTree.Node<?> node) {
        var data = node.asString();
        Minecraft.getInstance().keyboardHandler.setClipboard(data);
    }

    public static NbtTree.Node<?> getNode() {
        var data = Minecraft.getInstance().keyboardHandler.getClipboard();
        return NbtTree.Node.fromString(data);
    }
}
