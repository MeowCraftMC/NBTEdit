package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemStackEditingPacket(CompoundTag tag, boolean readOnly, ItemStack itemStack) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.ITEM_STACK_EDITING_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
        buf.writeBoolean(readOnly);
        buf.writeItem(itemStack);
    }

    public static ItemStackEditingPacket read(FriendlyByteBuf buf) {
        return new ItemStackEditingPacket(buf.readNbt(), buf.readBoolean(), buf.readItem());
    }
}
