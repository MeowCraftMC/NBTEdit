package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class C2SBlockEntityEditingRequestPacket {

	/**
	 * The position of the BlockEntity which was requested.
	 */
	private BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
		C2SBlockEntityEditingRequestPacket packet = new C2SBlockEntityEditingRequestPacket(friendlyByteBuf);
		NBTEditEditingHelper.editBlockEntity(player, packet.pos);
	}
}
