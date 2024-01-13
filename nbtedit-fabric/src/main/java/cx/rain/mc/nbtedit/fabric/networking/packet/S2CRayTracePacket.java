package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class S2CRayTracePacket implements FabricPacket {
	public static final PacketType<S2CRayTracePacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.S2C_RAY_TRACE_REQUEST_PACKET_ID, S2CRayTracePacket::new);

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

	public static void clientHandle(Minecraft client, ClientPacketListener handler,
									FriendlyByteBuf buf, PacketSender responseSender) {
		RayTraceHelper.doRayTrace();
	}
}
