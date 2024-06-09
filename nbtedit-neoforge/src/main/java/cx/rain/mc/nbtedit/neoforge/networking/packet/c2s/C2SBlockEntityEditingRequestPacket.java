package cx.rain.mc.nbtedit.neoforge.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class C2SBlockEntityEditingRequestPacket implements CustomPacketPayload {
	private final BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	@Override
	public @NotNull ResourceLocation id() {
		return NetworkingConstants.C2S_BLOCK_ENTITY_RAYTRACE_RESULT_PACKET_ID;
	}

	public BlockPos getPos() {
		return pos;
	}
}
