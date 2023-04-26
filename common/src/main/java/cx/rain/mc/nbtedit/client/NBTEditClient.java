package cx.rain.mc.nbtedit.client;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.nbt.ClipboardStates;
import net.minecraft.client.Minecraft;

import java.nio.file.Paths;

public class NBTEditClient {
	private NBTTree.Node<?> clipboard = null;
	private ClipboardStates clipboardSaves;

    public NBTEditClient() {
        clipboardSaves = new ClipboardStates(Paths.get(".", "nbtedit", "Clipboard.nbt").toFile());

        // Fixme: it is not working.
//        ClipboardStates clipboard = getClipboardSaves();
//        clipboard.load();
//        clipboard.save();
    }

	public ClipboardStates getClipboardSaves() {
		return clipboardSaves;
	}

    // Todo: fromString
    public NBTTree.Node<?> getClipboard() {
        return clipboard;
    }

    // Todo: toString
    public void setClipboard(NBTTree.Node<?> clipboard) {
        this.clipboard = clipboard;
    }
}
