package cx.rain.mc.nbtedit.forge.networking.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemStackEditPacket extends AbstractCompoundTagPacket {
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

    public ItemStack getItemStack() {
        return itemStack;
    }
}
