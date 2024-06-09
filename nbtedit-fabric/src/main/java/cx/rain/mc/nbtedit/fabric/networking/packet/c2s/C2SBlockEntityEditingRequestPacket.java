package cx.rain.mc.nbtedit.fabric.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class C2SBlockEntityEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SBlockEntityEditingRequestPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.C2S_BLOCK_ENTITY_RAYTRACE_RESULT_PACKET_ID, C2SBlockEntityEditingRequestPacket::new);

	private final BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
	}

	public BlockPos getPos() {
		return pos;
	}
}
