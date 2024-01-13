package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class C2SItemStackEditingRequestPacket {
	private ItemStack itemStack;

	public C2SItemStackEditingRequestPacket(ItemStack stack) {
		itemStack = stack;
	}

	public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
		itemStack = buf.readItem();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeItem(itemStack);
	}

	public void serverHandleOnMain(CustomPayloadEvent.Context context) {
        var player = context.getSender();
		NBTEditEditingHelper.editItemStack(player, itemStack);
	}
}
