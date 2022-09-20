package cx.rain.mc.nbtedit.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SBlockEntityEditingRequestPacket {
	/**
	 * The position of the BlockEntity which was requested.
	 */
	private BlockPos pos;

	public C2SBlockEntityEditingRequestPacket(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		pos = buf.readBlockPos();
	}

	public C2SBlockEntityEditingRequestPacket(BlockPos posIn) {
		pos = posIn;
	}

	public void toBytes(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		buf.writeBlockPos(pos);
	}

	public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " requested BlockEntity at " +
				pos.getX() + " " + pos.getY() + " " + pos.getZ() + ".");

		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_BLOCK_ENTITY,
				pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.GREEN));
		NBTEdit.getInstance().getNetworkManager().serverOpenClientGui(player, pos);
	}
}
