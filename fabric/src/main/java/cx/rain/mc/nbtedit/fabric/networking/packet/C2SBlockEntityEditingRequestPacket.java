package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

	public FriendlyByteBuf write() {
		var buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		return buf;
	}

	public static void serverHandle(MinecraftServer server, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf buf, PacketSender sender) {
		var packet = new C2SBlockEntityEditingRequestPacket(buf);

		NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " requested BlockEntity at " +
				packet.pos.getX() + " " + packet.pos.getY() + " " + packet.pos.getZ() + ".");

		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_BLOCK_ENTITY,
				packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()).withStyle(ChatFormatting.GREEN));
		NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, packet.pos);
	}
}
