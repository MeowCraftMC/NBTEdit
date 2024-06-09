package cx.rain.mc.nbtedit.fabric.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackEditingRequestPacket implements FabricPacket {
	public static final PacketType<C2SItemStackEditingRequestPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.C2S_ITEM_STACK_RAYTRACE_RESULT_PACKET_ID, C2SItemStackEditingRequestPacket::new);

	private final ItemStack itemStack;

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

	public ItemStack getItemStack() {
		return itemStack;
	}
}
