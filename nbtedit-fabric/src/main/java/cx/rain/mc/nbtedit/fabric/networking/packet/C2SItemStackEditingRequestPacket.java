package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
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

	public void write(FriendlyByteBuf buf) {
		buf.writeItem(itemStack);
	}

	public static void serverHandle(MinecraftServer minecraftServer, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
		var packet = new C2SItemStackEditingRequestPacket(friendlyByteBuf);
		NBTEditEditingHelper.editItemStack(player, packet.itemStack);
	}
}
