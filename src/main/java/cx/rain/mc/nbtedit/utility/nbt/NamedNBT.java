package cx.rain.mc.nbtedit.utility.nbt;

import net.minecraft.nbt.Tag;

public class NamedNBT {
	protected String name;
	protected Tag tag;

	public NamedNBT(Tag tag) {
		this("", tag);
	}

	public NamedNBT(String name, Tag tag) {
		this.name = name;
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag nbt) {
		this.tag = nbt;
	}

	public NamedNBT copy() {
		return new NamedNBT(name, tag.copy());
	}
}
