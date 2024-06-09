package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemStackEditPacket extends AbstractCompoundTagPacket implements FabricPacket {
    public static final PacketType<ItemStackEditPacket> PACKET_TYPE = PacketType.create(NetworkingConstants.ITEM_STACK_EDITING_PACKET_ID, ItemStackEditPacket::new);

    private final ItemStack itemStack;

    public ItemStackEditPacket(FriendlyByteBuf buf) {
        super(buf);
        itemStack = buf.readItem();
    }

    public ItemStackEditPacket(CompoundTag tag, boolean readOnly, ItemStack stack) {
        super(tag, readOnly);
        itemStack = stack;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        super.write(buf);
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
