package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class S2CRayTracePacket {

	public S2CRayTracePacket() {
	}

	public S2CRayTracePacket(FriendlyByteBuf buf) {
	}

	public void write(FriendlyByteBuf buf) {
	}

	public static void clientHandle(Minecraft client, ClientPacketListener handler,
									FriendlyByteBuf buf, PacketSender responseSender) {
		RayTraceHelper.doRayTrace();
	}
}
