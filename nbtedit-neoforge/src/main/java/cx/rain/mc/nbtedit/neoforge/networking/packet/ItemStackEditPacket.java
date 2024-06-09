package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackEditPacket extends AbstractCompoundTagPacket implements CustomPacketPayload {
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
    public void write(@NotNull FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeItem(itemStack);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NetworkingConstants.ITEM_STACK_EDITING_PACKET_ID;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
