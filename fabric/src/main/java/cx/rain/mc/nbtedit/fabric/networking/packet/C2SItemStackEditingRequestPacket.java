package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackEditingRequestPacket {
	private ItemStack itemStack;

	public C2SItemStackEditingRequestPacket(ItemStack stack) {
		itemStack = stack;
	}

	public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
		itemStack = buf.readItem();
	}

	public FriendlyByteBuf write() {
		var buf = PacketByteBufs.create();
		buf.writeItem(itemStack);
		return buf;
	}

	public static void serverHandle(MinecraftServer server, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf buf, PacketSender sender) {
		var packet = new C2SItemStackEditingRequestPacket(buf);

		NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
				" requested to edit ItemStack named " + packet.itemStack.getDisplayName().getString() + ".");
		player.sendMessage(new TranslatableComponent(Constants.MESSAGE_EDITING_ITEM_STACK,
						packet.itemStack.getDisplayName().getString())
				.withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
		NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, packet.itemStack);
	}
}
