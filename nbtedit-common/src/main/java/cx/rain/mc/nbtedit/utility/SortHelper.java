package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.nbt.NBTTree;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Comparator;

public class SortHelper implements Comparator<NBTTree.Node<?>> {
	private static SortHelper INSTANCE;

	public static SortHelper get() {
		if (INSTANCE == null) {
			return new SortHelper();
		}
		return INSTANCE;
	}

	public SortHelper() {
		INSTANCE = this;
	}

	@Override
	public int compare(NBTTree.Node<?> a, NBTTree.Node<?> b) {
		var name1 = a.getName();
		var name2 = b.getName();
		var tag1 = a.getTag();
		var tag2 = b.getTag();
		if (tag1 instanceof CompoundTag || tag1 instanceof ListTag) {
			if (tag2 instanceof CompoundTag || tag2 instanceof ListTag) {
				int difference = tag1.getId() - tag2.getId();
				return (difference == 0) ? name1.compareTo(name2) : difference;
			}
			return 1;
		}
		if (tag2 instanceof CompoundTag || tag2 instanceof ListTag) {
			return -1;
		}
		int difference = tag1.getId() - tag2.getId();
		return (difference == 0) ? name1.compareTo(name2) : difference;
	}
}
