package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2SBlockEntityEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SBlockEntityEditingRequestPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_BLOCK_ENTITY_EDITING_PACKET_ID, C2SBlockEntityEditingRequestPacket::new);

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

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
	}

	public static void serverHandle(C2SBlockEntityEditingRequestPacket packet,
							 ServerPlayer player, PacketSender responseSender) {
		NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " requested BlockEntity at " +
				packet.pos.getX() + " " + packet.pos.getY() + " " + packet.pos.getZ() + ".");

		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_BLOCK_ENTITY,
				packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()).withStyle(ChatFormatting.GREEN));
		NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, packet.pos);
	}
}
