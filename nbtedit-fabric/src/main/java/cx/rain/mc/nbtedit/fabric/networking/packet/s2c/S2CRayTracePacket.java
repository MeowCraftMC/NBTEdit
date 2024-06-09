package cx.rain.mc.nbtedit.fabric.networking.packet.s2c;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public class S2CRayTracePacket implements FabricPacket {
	public static final PacketType<S2CRayTracePacket> PACKET_TYPE = PacketType.create(NetworkingConstants.S2C_RAYTRACE_REQUEST_PACKET_ID, S2CRayTracePacket::new);

	public S2CRayTracePacket() {
	}

	public S2CRayTracePacket(FriendlyByteBuf buf) {
	}

	@Override
	public void write(FriendlyByteBuf buf) {
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
	}
}
