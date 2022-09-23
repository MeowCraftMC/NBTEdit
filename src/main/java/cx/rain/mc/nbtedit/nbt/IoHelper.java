package cx.rain.mc.nbtedit.nbt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IoHelper {
	public static CompoundTag read(DataInputStream in) throws IOException {
		return NbtIo.read(in);
	}

	public static void write(CompoundTag compound, DataOutput out) throws IOException {
		NbtIo.write(compound, out);
	}

	public static Map<String, Tag> getMap(CompoundTag tag) {
		return ObfuscationReflectionHelper.getPrivateValue(CompoundTag.class, tag, "tags");
	}

	public static Tag getTagAt(ListTag tag, int index) {
		List<Tag> list = ObfuscationReflectionHelper.getPrivateValue(ListTag.class, tag, "list");
		return list.get(index);
	}

	public static void write(CompoundTag nbt, ByteBuf buf) {
		if (nbt == null) {
			buf.writeByte(0);
		} else {
			try {
				NbtIo.write(nbt, new ByteBufOutputStream(buf));
			} catch (IOException e) {
				throw new EncoderException(e);
			}
		}
	}

	public static CompoundTag read(ByteBuf buf) {
		int index = buf.readerIndex();
		byte isNull = buf.readByte();

		if (isNull == 0) {
			return null;
		} else {
			// restore index after checking to make sure the tag wasn't null/
			buf.readerIndex(index);
			try {
				return NbtIo.read(new ByteBufInputStream(buf), new NbtAccounter(2097152L));
			} catch (IOException ioexception) {
				throw new EncoderException(ioexception);
			}
		}
	}
}
