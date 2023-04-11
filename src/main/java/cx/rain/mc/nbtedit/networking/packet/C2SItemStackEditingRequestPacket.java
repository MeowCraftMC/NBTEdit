package cx.rain.mc.nbtedit.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SItemStackEditingRequestPacket {
	private ItemStack itemStack;

	public C2SItemStackEditingRequestPacket(ItemStack stack) {
		itemStack = stack;
	}

	public C2SItemStackEditingRequestPacket(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		itemStack = buf.readItem();
	}

	public void toBytes(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		buf.writeItem(itemStack);
	}

	public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " requested to edit ItemStack named " + itemStack.getDisplayName().getString() + ".");
		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_ITEM_STACK,
						itemStack.getDisplayName().getString())
				.withStyle(ChatFormatting.GREEN));
        NBTEdit.getInstance().getNetworkManager().serverOpenClientGui(player, itemStack);
	}
}
