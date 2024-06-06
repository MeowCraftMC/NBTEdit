package cx.rain.mc.nbtedit.client;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.Tag;

@Deprecated(forRemoval = true)
public class NBTEditClient {
    public NBTEditClient() {
    }

    public NBTTree.Node<Tag> getClipboard() {
        var data = Minecraft.getInstance().keyboardHandler.getClipboard();
        return NBTTree.Node.fromString(data);
    }

    public void setClipboard(NBTTree.Node<?> node) {
        var data = node.asString();
        Minecraft.getInstance().keyboardHandler.setClipboard(data);
    }
}
