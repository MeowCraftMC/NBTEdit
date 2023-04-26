package cx.rain.mc.nbtedit.utility.nbt;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

// This save format can definitely be improved. Also, this can be extended to provide infinite save slots - just
// need to add some scrollbar (use GuiLib!).
// Todo: AS: Infinite save slots.
public class ClipboardStates {
	private File file;
	private Clipboard[] tags;

	public ClipboardStates(File file) {
		this.file = file;
		tags = new Clipboard[7];
		for (int i = 0; i < 7; ++i)
			tags[i] = new Clipboard("Slot " + (i + 1));
	}

	public void read() throws IOException {
		if (file.exists() && file.canRead()) {
			CompoundTag root = NbtIo.read(file);
			for (int i = 0; i < 7; ++i) {
				String name = "slot" + (i + 1);
				if (root.contains(name))
					tags[i].tag = root.getCompound(name);
				if (root.contains(name + "Name"))
					tags[i].name = root.getString(name + "Name");
			}
		}
	}

	public void write() throws IOException {
		CompoundTag root = new CompoundTag();
		for (int i = 0; i < 7; ++i) {
			root.put("slot" + (i + 1), tags[i].tag);
			root.putString("slot" + (i + 1) + "Name", tags[i].name);
		}
		NbtIo.write(root, file);
	}

	public void save() {
		try {
			write();
			NBTEdit.getInstance().getLogger().info("NBTEdit saved successfully.");
		} catch (IOException ex) {
			NBTEdit.getInstance().getLogger().error(new RuntimeException("Unable to save NBTEdit save.", ex).toString());
		}
	}

	public void load() {
		try {
			read();
			NBTEdit.getInstance().getLogger().info("NBTEdit save loaded successfully.");
		} catch (IOException ex) {
			NBTEdit.getInstance().getLogger().error(new RuntimeException("Unable to load NBTEdit save.", ex).toString());
		}
	}

	public Clipboard getClipboard(int index) {
		return tags[index];
	}

	public static final class Clipboard {
		public String name;
		public CompoundTag tag;

		public Clipboard(String name) {
			this.name = name;
			this.tag = new CompoundTag();
		}
	}
}
