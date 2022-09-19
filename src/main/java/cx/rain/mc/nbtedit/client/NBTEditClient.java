package cx.rain.mc.nbtedit.client;

import cx.rain.mc.nbtedit.utility.nbt.ClipboardStates;
import cx.rain.mc.nbtedit.utility.nbt.NamedNBT;

import java.nio.file.Paths;

public class NBTEditClient {
	private NamedNBT clipboard = null;
	private ClipboardStates clipboardSaves;

    public NBTEditClient() {
        clipboardSaves = new ClipboardStates(Paths.get(".", "nbtedit", "Clipboard.nbt").toFile());

        ClipboardStates clipboard = getClipboardSaves();
        clipboard.load();
        clipboard.save();
    }

	public ClipboardStates getClipboardSaves() {
		return clipboardSaves;
	}

    public NamedNBT getClipboard() {
        return clipboard;
    }

    public void setClipboard(NamedNBT clipboard) {
        this.clipboard = clipboard;
    }
}
