package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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

	public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		NBTEditEditingHelper.editItemStack(player, itemStack);
	}
}
