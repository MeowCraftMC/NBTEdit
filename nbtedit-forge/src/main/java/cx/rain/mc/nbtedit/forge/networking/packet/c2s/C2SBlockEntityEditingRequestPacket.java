package cx.rain.mc.nbtedit.forge.networking.packet.c2s;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class C2SBlockEntityEditingRequestPacket {
	private final BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	public BlockPos getPos() {
		return pos;
	}
}
