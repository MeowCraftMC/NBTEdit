package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SItemStackEditingRequestPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID, C2SItemStackEditingRequestPacket::new);

	private ItemStack itemStack;

	public C2SItemStackEditingRequestPacket(ItemStack stack) {
		itemStack = stack;
	}

	public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
		itemStack = buf.readItem();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeItem(itemStack);
	}

	@Override
	public PacketType<?> getType() {
		return PACKET_TYPE;
	}

	public static void serverHandle(C2SItemStackEditingRequestPacket packet,
								   ServerPlayer player, PacketSender responseSender) {
        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " requested to edit ItemStack named " + packet.itemStack.getDisplayName().getString() + ".");
		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_ITEM_STACK,
						packet.itemStack.getDisplayName().getString())
				.withStyle(ChatFormatting.GREEN));
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, packet.itemStack);
	}
}
