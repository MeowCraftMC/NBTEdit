package cx.rain.mc.nbtedit.client;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.Tag;

public class NBTEditClient {
    public NBTEditClient() {
    }

    public NBTTree.Node<Tag> getClipboard() {
        String data = Minecraft.getInstance().keyboardHandler.getClipboard();
        return NBTTree.Node.fromString(data);
    }

    public void setClipboard(NBTTree.Node<?> node) {
        String data = node.asString();
        Minecraft.getInstance().keyboardHandler.setClipboard(data);
    }
}
