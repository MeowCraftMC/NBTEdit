package cx.rain.mc.nbtedit.forge.networking.packet.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackEditingRequestPacket {
	private final ItemStack itemStack;

	public C2SItemStackEditingRequestPacket(ItemStack stack) {
		itemStack = stack;
	}

	public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
		itemStack = buf.readItem();
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeItem(itemStack);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
}
